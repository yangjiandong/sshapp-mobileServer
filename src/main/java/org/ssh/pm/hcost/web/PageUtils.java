package org.ssh.pm.hcost.web;

import java.util.Map;

import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.hcost.service.CommonService;

public class PageUtils {

    public static Map<String, Object> getApplicationInfos() {
        CommonService manager = (CommonService) SpringContextHolder.getBean("commonService");
        return manager.getApplicationInfos();
    }
}
