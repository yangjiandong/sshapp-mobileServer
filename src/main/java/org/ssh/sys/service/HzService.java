package org.ssh.sys.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.memcached.SpyMemcachedClient;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.common.cache.CacheUtil;
import org.ssh.pm.common.cache.MemcachedObjectType;
import org.ssh.sys.dao.HzDao;
import org.ssh.sys.entity.Hz;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

@Service
@Transactional
public class HzService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private HzDao hzDao;

    private SpyMemcachedClient spyMemcachedClient;
    private JsonMapper jsonBinder = JsonMapper.buildNonDefaultMapper();

    @Autowired(required = false)
    public void setSpyMemcachedClient(SpyMemcachedClient spyMemcachedClient) {
        this.spyMemcachedClient = spyMemcachedClient;
    }

    @Autowired
    public void setHzDao(HzDao hzDao) {
        this.hzDao = hzDao;
    }

    /**
     * 获取字符串的五笔助记符和拼音助记符,其中的英文字母和阿拉伯数字保持不变
     */
    public Map<String, String> getMemo(String hzStr) {
        Map<String, String> map = new HashMap<String, String>();
        String oneS = null;
        StringBuffer bfWb = new StringBuffer();
        StringBuffer bfPy = new StringBuffer();
        Hz hz = null;
        for (int i = 0; i < hzStr.length(); i++) {
            oneS = hzStr.substring(i, i + 1);
            if (spyMemcachedClient != null) {
                logger.debug("get hz use memecache!!!" + oneS);
                hz = getHzFromMemcached(oneS);
            } else {

                //hz = hzDao.findOne(oneS);
                hz = getHzFromEhcache(oneS);

            }
            if (hz != null) {
                bfWb.append(hz.getWb().substring(0, 1));
                bfPy.append(hz.getPy().substring(0, 1));
            }
        }

        String py = bfPy.toString();
        String wb = bfWb.toString();

        if (py.length() > 10) {
            py = py.substring(0, 10);
        }

        if (wb.length() > 10) {
            wb = wb.substring(0, 10);
        }

        map.put("py", py);
        map.put("wb", wb);

        return map;
    }

    /**
     * 获取字符串的五笔助记符和拼音助记符,其中的英文字母和阿拉伯数字保持不变
     */
    public Hz getHz(String hzStr) {
        String oneS = null;
        Hz hz = null;
        for (int i = 0; i < hzStr.length(); i++) {
            oneS = hzStr.substring(i, i + 1);
            if (spyMemcachedClient != null) {
                logger.debug("get hz use memecache!!!" + oneS);
                hz = getHzFromMemcached(oneS);
            } else {
                hz = getHzFromEhcache(oneS);
            }
        }

        return hz;
    }

    /**
     * 访问Memcached, 使用JSON字符串存放对象以节约空间.
     */
    private Hz getHzFromMemcached(String one) {
        //TODO
        //不能直接用汉字作为spyMemcachedClient的key
        String one_code = one;
        try {
            one_code = URLEncoder.encode(one, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        String key = MemcachedObjectType.HZK.getPrefix() + one_code;

        Hz hz = null;
        String jsonString = spyMemcachedClient.get(key);
        logger.debug("get key:" + key + ",jsonString:" + jsonString);
        if (jsonString == null) {
            //hz = hzDao.findOne(one);
            hz = hzDao.findOneBy("hz", one);
            if (hz != null) {
                jsonString = jsonBinder.toJson(hz);
                spyMemcachedClient.set(key, MemcachedObjectType.HZK.getExpiredTime(), jsonString);
            }
        } else {
            hz = jsonBinder.fromJson(jsonString, Hz.class);
        }
        return hz;
    }

    /**
     * 访问ehcache, 使用JSON字符串存放对象以节约空间.
     */
    private Hz getHzFromEhcache(String one) {
        //TODO
        //不能直接用汉字作为spyMemcachedClient的key
        String one_code = one;
        try {
            one_code = URLEncoder.encode(one, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        String key = CacheUtil.HZK + one_code;

        Hz hz = null;
        String jsonString = (String) CacheUtil.getCache(CacheUtil.HZK, key);
        logger.debug("get key:" + key + ",jsonString:" + jsonString);
        if (jsonString == null) {
            //hz = hzDao.findOne(one);
            hz = hzDao.findOneBy("hz", one);
            if (hz != null) {
                jsonString = jsonBinder.toJson(hz);
                //spyMemcachedClient.set(key, MemcachedObjectType.HZK.getExpiredTime(), jsonString);

                CacheUtil.setCache(CacheUtil.HZK, key, jsonString);
            }
        } else {
            hz = jsonBinder.fromJson(jsonString, Hz.class);
        }
        return hz;
    }

    public void initData() {
        if (this.hzDao.getHzCount() != 0)
            return;

        logger.debug("开始装载汉字库数据");

        File resourcetxt = new File(this.getClass().getResource("/data/hzk.txt").getFile());
        try {
            //this.hzDao.getSession().beginTransaction();

            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            Hz re = new Hz();
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                String star[] = thisLine.split(",");
                if (star[1].trim().equals(""))
                    continue;

                if (hzDao.findUniqueBy("hz", star[1]) != null)
                    continue;

                re = new Hz();
                re.setId(Long.valueOf(star[0]));
                re.setHz(star[1]);
                re.setPy(star[2]);
                re.setWb(star[3]);
                this.hzDao.save(re);
            }

            //this.hzDao.getSession().getTransaction().commit();
        } catch (Exception e) {
            //this.hzDao.getSession().getTransaction().rollback();
            logger.error("装载汉字数据出错:" + e);
            throw new ServiceException("导入汉字库时，服务器发生异常");
        } finally {

        }
    }

    //采用了方法缓存
    @Transactional(readOnly = true)
    public List<Hz> getHzsOnMethodCache() {
        //return bookDao.getAll();
        StringBuffer bf = new StringBuffer();
        bf.append("select * ");
        bf.append(" from  T_HZK where hz='饭'");

        SQLQuery query = this.hzDao.getSession().createSQLQuery(bf.toString());
        return query.list();
    }

    // 要采用批量插入
    public void initDataByBatch() throws ServiceException {
        if (this.hzDao.getHzCount() != 0)
            return;

        File resourcetxt = new File(this.getClass().getResource("/data/hzk.txt").getFile());

        Session session = null;
        try {
            // use google guava
            List<String> lines = Files.readLines(resourcetxt, Charsets.UTF_8);

            session = this.hzDao.getSession();
            session.setCacheMode(CacheMode.IGNORE);
            session.beginTransaction();

            String s0;
            Hz re;
            Long i = 1L;
            int line = 1;
            for (String thisLine : lines) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                String star[] = thisLine.split(",");
                if (star[1].trim().equals(""))
                    continue;

                s0 = star[1].trim();

                if (s0.equals(""))
                    continue;

                re = new Hz();
                re.setId(Long.valueOf(star[0]));
                re.setHz(s0);
                re.setPy(star[2]);
                re.setWb(star[3]);

                this.hzDao.save(re);

                if (i % 100 == 0) {
                    session.flush();
                    session.clear();
                }
                i++;
            }
            session.flush();
            session.clear();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("装载汉字数据出错:", e);
            throw new ServiceException("导入汉字库时，服务器发生异常");
        } finally {
        }
    }

    ////采用了方法缓存
    @Transactional(readOnly = true)
    public List<Hz> getAllHzOnMethodCache() {
        List<Hz> list = hzDao.getAll();
        logger.info("get {} hz sucessful.", list.size());
        return list;
    }
}