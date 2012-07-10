package org.ssh.tool.httpclient;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
* 最简单的HTTP客户端,用来演示通过GET或者POST方式访问某个页面
* @author Liudong
*/
public class SimpleClient2 {
    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost("www.imobile.com.cn", 80, "http");
        HttpMethod method = getPostMethod();//使用POST方式提交数据
        client.executeMethod(method);
        //打印服务器返回的状态
        System.out.println(method.getStatusLine());
        //打印结果页面
        String response = new String(method.getResponseBodyAsString().getBytes("8859_1"));
        //打印返回的信息
        System.out.println(response);
        method.releaseConnection();
    }

    private static HttpMethod getGetMethod() {
        return new GetMethod("/simcard.php?simcard=1330227");
    }

    private static HttpMethod getPostMethod() {
        PostMethod post = new PostMethod("/simcard.php");
        NameValuePair simcard = new NameValuePair("simcard", "1330227");
        post.setRequestBody(new NameValuePair[] { simcard });
        return post;
    }
}