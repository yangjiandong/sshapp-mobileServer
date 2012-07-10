package org.ssh.pm.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.orm.Page;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.enums.SetupCode;
import org.ssh.sys.entity.AppSetup;
import org.ssh.sys.service.AppSetupService;

//拦截器，强制给url增加limit属性
public class SetPageSizeHandlerInterceptor implements HandlerInterceptor {
    //extends org.springframework.web.servlet.handler.HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse response, Object handler) throws Exception {

        String className = handler.getClass().getName();
        if (className.startsWith("org.ssh")) {
            String pageSize = req.getParameter(Page.GRID_PARAM_PAGESIZE);
            if (StringUtils.isBlank(pageSize)) {

                AppSetupService appSetupService = SpringContextHolder.getBean("appSetupService");
                AppSetup a = appSetupService.getAppSetup(SetupCode.DEFAULT_PAGE.value());
                String page = "20";
                if (a != null) {
                    page = a.getSetupValue();
                }

                req.setAttribute(Page.GRID_PARAM_PAGESIZE, page);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }
}