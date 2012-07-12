package org.ssh.pm.mob.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.mob.entity.MobLog;
import org.ssh.pm.mob.service.MobLogService;
import org.ssh.sys.service.AccountManager;

import com.ek.mobileapp.model.MobLogDTO;

@Controller
@RequestMapping("/moblog")
public class MobLogController {
    private static Logger logger = LoggerFactory.getLogger(MobLogController.class);

    @Autowired
    private MobLogService mobLogService;
    @Autowired
    private DozerBeanMapper dozer;

    @RequestMapping("/save")
    public @ResponseBody
    Map<String, Object> saveCron(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String infos = request.getParameter("infos");
            JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
            MobLogDTO entity = mapper.fromJson(infos, MobLogDTO.class);

            MobLog u = dozer.map(entity, MobLog.class);
            AccountManager manager = (AccountManager) SpringContextHolder.getBean("accountManager");
            u.setCreateTime(manager.getNowString());
            this.mobLogService.save(u);

            map.put("success", true);
            map.put("message", "");
        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
