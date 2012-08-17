package org.ssh.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssh.sys.service.MigrationService;
import org.ssh.sys.web.CommonController.Bean;

/**
 * 后台脚本升级
 */
@Controller
@RequestMapping("/migration")
public class MigrationController {
    private static Logger logger = LoggerFactory.getLogger(MigrationController.class);

    @Autowired
    MigrationService migrationService;

    @RequestMapping("/get_migrations")
    public @ResponseBody
    Map<String, Object> getMigrations(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        Long c = migrationService.countData();

        re.put("success", true);
        re.put("count", c);
        return re;
    }

    @RequestMapping("/process_migrations")
    public @ResponseBody
    Map<String, Object> processMigrations(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        long start = System.currentTimeMillis();

        List<Bean> data = migrationService.processMigration();

        map.put("datas", data);
        map.put("times", (System.currentTimeMillis() - start) + " ms");
        map.put("totalCount", data.size());
        return map;
    }
}
