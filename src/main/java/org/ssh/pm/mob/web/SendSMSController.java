package org.ssh.pm.mob.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssh.pm.mob.service.SendSMSService;

@Controller
@RequestMapping("/sms")
public class SendSMSController {
    private static Logger logger = LoggerFactory.getLogger(SendSMSController.class);

    @Autowired
    private SendSMSService sendSMSService;

    @RequestMapping("/send")
    public @ResponseBody
    Map<String, Object> internlService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> re = new HashMap<String, Object>();
        boolean b = true;
        String msg = "";
        String content = request.getParameter("content");
        String[] ids = request.getParameterValues("uid");
        try {
            sendSMSService.sendSMSToUsers(content, ids);
        } catch (Exception e) {
            logger.error("internlService", e);
            b = false;
            msg = "发送短信出错";
        }
        re.put("seccess", b);
        re.put("msg", msg);
        return re;
    }
}
