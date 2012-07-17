package org.ssh.pm.mob.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.common.utils.AppUtil;
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

    @RequestMapping("/query")
    public @ResponseBody
    Map<String, Object> query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        Page<MobLog> page = new Page<MobLog>(request);
        String orderColumn = request.getParameter("sort");
        String dir = request.getParameter("dir");
        if (StringUtils.isBlank(orderColumn)) {
            page.setOrderBy("createTime");
            page.setOrder(Page.DESC);

        } else {
            page.setOrderBy(orderColumn);
            page.setOrder(dir);
        }
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        String userId = request.getParameter("userId");
        if (StringUtils.isNotEmpty(userId)) {
            PropertyFilter filter = new PropertyFilter("EQL_userId", userId);
            filters.add(filter);
        }
        Page<MobLog> data = mobLogService.query(page, filters);
        map = AppUtil.buildJSONDataResponse(data.getResult(), (long) data.getTotalCount());
        return map;
    }
}
