package org.ssh.pm.common.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.sys.service.HzService;

@SuppressWarnings("serial")
public class DemoServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "application/xml; charset=UTF-8";

    //作为spring bean 定义，该方法不会被调用
    @Override
    public void init(ServletConfig config) throws ServletException {
        //ServletConfigFactoryBean.setServletConfig(config);

        ServletContext context = config.getServletContext();
        //contextPath = context.getContextPath() + "/servlet/hidden_comet";
        System.out.println(context.getContextPath() + "/servlet/hidden_comet");
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        HzService hzService = (HzService) SpringContextHolder.getBean("hzService");
        Map<String, String> hz = hzService.getMemo("我");

        String result = "我，五笔:" + hz.get("wb");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<root>\n");
        out.println("<result>" + result + "</result>\n");
        out.println("</root>\n");

        out.flush();
        out.close();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}