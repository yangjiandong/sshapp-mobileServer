package org.ssh.sys.service;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.orm.hibernate.EntityService;
import org.ssh.sys.dao.MigrationDao;
import org.ssh.sys.dao.UserJdbcDao;
import org.ssh.sys.entity.Migration;
import org.ssh.sys.web.CommonController.Bean;

@Service
public class MigrationService extends EntityService<Migration, Long> {
    protected static Logger logger = LoggerFactory.getLogger(MigrationService.class);
    private static Logger oneLogger = LoggerFactory.getLogger("org.ssh.pms.times");

    @Autowired
    MigrationDao migrationDao;
    @Autowired
    private UserJdbcDao userJdbcDao;

    @Override
    protected MigrationDao getEntityDao() {
        return migrationDao;
    }

    @Transactional(readOnly = true)
    public Long countData() {
        return this.migrationDao.getCount();
    }

    @Transactional
    public void initData() {

        logger.debug("开始装载后台更新脚本");

        File xmlFile = new File(this.getClass().getResource("/data/migration/update.xml").getFile());

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlFile);

            List childNodes = document.selectNodes("//updates/update");
            for (Object object : childNodes) {
                Element elm = (Element) object;
                org.dom4j.Attribute id = elm.attribute("id");
                org.dom4j.Attribute name = elm.attribute("name");
                org.dom4j.Attribute descrip = elm.attribute("descrip");
                org.dom4j.Attribute type = elm.attribute("sqlType");

                Migration re = new Migration();
                if (migrationDao.findUniqueBy("id", Long.valueOf(id.getText())) != null)
                    continue;

                re = new Migration();
                re.setId(Long.valueOf(id.getText()));
                re.setName(name.getText());
                re.setDescrip(descrip.getText());
                re.setSqlType(type.getText());
                re.setStatus("0");
                re.setCreateBy("初始化");
                re.setCreateTime(userJdbcDao.getNowString());
                this.migrationDao.save(re);

            }

        } catch (Exception e) {
            logger.error("装载后台更新脚本出错:" + e);
            throw new ServiceException("导入后台更新脚本时，服务器发生异常");
        } finally {

        }
    }

    public List<Bean> processMigration() {
        List<Bean> data = new ArrayList<Bean>();

        List<Migration> alls = find("from Migration where status=? order by id", "0");
        for (Migration one : alls) {
            Bean b = processOneMigration(one);
            data.add(b);
            if (b.getSuccess()) {

                one.setStatus("1");
                one.setRunBy("更新");
                one.setRunTime(userJdbcDao.getNowString());
                one.setSqlS(b.getNotes());
                this.migrationDao.save(one);
            } else {
                //one.setSqlS(b.getNotes());
                //this.migrationDao.save(one);
                break;
            }

        }
        return data;
    }

    public Bean processOneMigration(Migration one) {
        DataSource dataSource = (DataSource) SpringContextHolder.getBean("dataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // 后台数据库
        String dbdriver;
        Connection con = null;
        try {
            con = DataSourceUtils.getConnection(dataSource);
            dbdriver = con.getMetaData().getDatabaseProductName();
        } catch (Exception se) {
            logger.error("getNowString:", se);
        } finally {
            // 安全释放
            DataSourceUtils.releaseConnection(con, dataSource);
        }

        String type = one.getSqlType();
        String name = one.getName();
        String id = one.getId() + "";
        String sql = "";

        boolean success = true;
        String msg = name + "-操作成功!";
        String info = name;

        File xmlFile = new File(this.getClass().getResource("/data/migration/update.xml").getFile());

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlFile);

            StringBuilder sb = new StringBuilder();
            sb.append("//updates/update[@id=");
            sb.append(id);
            sb.append("]");
            Element elm = (Element) document.selectSingleNode(sb.toString());
            if (elm != null) {
                oneLogger.info("{},{}-开始更新", id, name);

                sql = elm.getTextTrim();
                if (type.equals(Migration.SQLTYPE_EXEC)) {
                    jdbcTemplate.execute(sql);
                } else if (type.equals(Migration.SQLTYPE_SQL)) {
                    jdbcTemplate.update(sql);
                } else if (type.equals(Migration.SQLTYPE_SP)) {
                    //保证存储过程格式
                    sql = elm.getText();
                    jdbcTemplate.execute(sql);
                }
            } else {
                logger.error("{},{}-更新没找到", id, name);
            }
        } catch (Exception e) {
            logger.error(name + "-更新失败:", e);
            success = false;
            msg = name + "-操作失败!" + e.getMessage();
            info = e.getMessage();
        }
        Bean bean = new Bean(success, msg, info);
        bean.setNotes(sql);//记录操作内容
        return bean;
    }
}
