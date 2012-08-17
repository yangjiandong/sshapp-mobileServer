package org.ssh.tool.httpclient;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
* 用来演示登录表单的示例
* @author Liudong
*/
public class FormLoginDemo {
    static final String LOGON_SITE = "localhost";
    static final int LOGON_PORT = 8080;

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);

        //模拟登录页面login.jsp->main.jsp
        PostMethod post = new PostMethod("/main.jsp");
        NameValuePair name = new NameValuePair("name", "ld");
        NameValuePair pass = new NameValuePair("password", "ld");
        post.setRequestBody(new NameValuePair[] { name, pass });
        int status = client.executeMethod(post);
        System.out.println(post.getResponseBodyAsString());
        post.releaseConnection();

        //查看cookie信息
        CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
        Cookie[] cookies = cookiespec.match(LOGON_SITE, LOGON_PORT, "/", false, client.getState().getCookies());
        if (cookies.length == 0) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.length; i++) {
                System.out.println(cookies[i].toString());
            }
        }
        //访问所需的页面main2.jsp
        GetMethod get = new GetMethod("/main2.jsp");
        client.executeMethod(get);
        System.out.println(get.getResponseBodyAsString());
        get.releaseConnection();
    }
}