package org.ssh.pm.mob.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.mob.dao.ModuleDictDao;
import org.ssh.pm.mob.dao.RoleModulesDao;
import org.ssh.pm.mob.entity.ModuleDict;
import org.ssh.pm.mob.entity.RoleModules;

@Service
@Transactional
public class ModuleDictService {
    private static Logger logger = LoggerFactory.getLogger(ModuleDictService.class);

    @Autowired
    private ModuleDictDao moduleDictDao;
    @Autowired
    private RoleModulesDao roleModulesDao;

    // 初始化,导入模块
    public void initData() throws ServiceException {
        // 判断新增
        logger.debug("开始装载模块初始数据");
        this.moduleDictDao.batchExecute("delete from ModuleDict");

        File resourcetxt = new File(this.getClass().getResource("/data/moduledict.txt").getFile());
        String star[] = {};
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            ModuleDict re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                // 第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                star = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                if (moduleDictDao.findUniqueBy("code", star[0].trim()) != null)
                    continue;

                re = new ModuleDict();
                re.setCode(star[0].trim());
                re.setName(star[1].trim());
                this.moduleDictDao.save(re);
            }
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载模块数据出错:" + e);
            throw new ServiceException("导入模块时，服务器发生异常");
        } finally {

        }
    }

    public List<JSONObject> queryModule(String roleId) {

        JSONObject json;
        List<JSONObject> result = new ArrayList<JSONObject>();
        List<ModuleDict> list1 = moduleDictDao.find(
                " from ModuleDict where code in (select moduleCode from RoleModules where roleId = ? )",
                Long.valueOf(roleId));
        List<ModuleDict> list2 = moduleDictDao.find(
                " from ModuleDict where code not in (select moduleCode from RoleModules where roleId = ? )",
                Long.valueOf(roleId));

        for (ModuleDict a : list1) {
            json = new JSONObject();
            json.put("moduleCode", a.getCode());
            json.put("moduleName", a.getName());
            json.put("hasChecked", CoreConstants.ACTIVE);
            result.add(json);
        }

        for (ModuleDict b : list2) {
            json = new JSONObject();
            json.put("moduleCode", b.getCode());
            json.put("moduleName", b.getName());
            json.put("hasChecked", CoreConstants.INACTIVE);
            result.add(json);
        }
        return result;

    }

    public void saveRoleModule(String roleId, String moduleCode) {

        try {

            List<RoleModules> list = roleModulesDao.find(" from RoleModules where roleId = ? and moduleCode = ? ",
                    Long.valueOf(roleId), moduleCode);
            if (list.size() == 0) {
                RoleModules entity = new RoleModules();
                entity.setModuleCode(moduleCode);
                entity.setRoleId(Long.valueOf(roleId));
                roleModulesDao.save(entity);
            }

        } catch (Exception e) {
            logger.error("saveRoleModule:" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }

    }

    public void delRoleModule(String roleId, String moduleCode) {

        try {

            List<RoleModules> list = roleModulesDao.find(" from RoleModules where roleId = ? and moduleCode = ? ",
                    Long.valueOf(roleId), moduleCode);
            if (list.size() > 0) {
                roleModulesDao.batchExecute(" delete from RoleModules where roleId = ? and moduleCode = ? ",
                        Long.valueOf(roleId), moduleCode);
            }

        } catch (Exception e) {
            logger.error("saveRoleModule:" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }

    }
}