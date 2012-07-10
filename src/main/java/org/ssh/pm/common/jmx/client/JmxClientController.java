package org.ssh.pm.common.jmx.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ssh.pm.common.jmx.client.JmxClientService.HibernateStatistics;

@Controller
@RequestMapping("/common")
public class JmxClientController {
    private static Logger logger = LoggerFactory.getLogger(JmxClientAction.class);

//    @Autowired
//    private JmxClientService jmxClientService;
//
//    //-- 页面属性 --//
//    private String nodeName;
//    private boolean notificationMailEnabled;
//    private HibernateStatistics hibernateStatistics;
//
//    /**
//     * 默认函数,显示服务器配置及运行情况.
//     */
//    @RequestMapping(value = "/jmx/getInfos")
//    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        nodeName = jmxClientService.getNodeName();
//        notificationMailEnabled = jmxClientService.isNotificationMailEnabled();
//        hibernateStatistics = jmxClientService.getHibernateStatistics();
//
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"jmx-nodeName\":" + nodeName + ",\"hibernate-openCount\":");
//        sb.append(hibernateStatistics.getSessionOpenCount());
//        sb.append(",\"hibernate-closeCount\":");
//        sb.append(hibernateStatistics.getSessionCloseCount());
//        sb.append(",\"hibernate-connectCount\":");
//        sb.append(hibernateStatistics.getConnectCount());
//        sb.append(",\"hibernate-objectName\":");
//        sb.append(hibernateStatistics.getStartTime());
//
//        sb.append("}");
//        response.setContentType("text/json; charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        out.write(sb.toString());
//    }
}
