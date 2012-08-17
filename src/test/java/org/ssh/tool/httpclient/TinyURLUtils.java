package org.ssh.tool.httpclient;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

//tiny short url
public abstract class TinyURLUtils {
    public static String getTinyUrl(String fullUrl) throws HttpException, IOException {
        HttpClient httpclient = new HttpClient();

        // Prepare a request object
        HttpMethod method = new GetMethod("http://tinyurl.com/api-create.php");
        method.setQueryString(new NameValuePair[] { new NameValuePair("url", fullUrl) });
        httpclient.executeMethod(method);
        String tinyUrl = method.getResponseBodyAsString();
        method.releaseConnection();
        return tinyUrl;
    }

    public static void main(String[] args) throws HttpException, IOException {
        String tinyUrl = TinyURLUtils.getTinyUrl("http://www.mularien.com/blog/");
        System.out.println(tinyUrl); // --> http://tinyurl.com/5cporq
    }
}