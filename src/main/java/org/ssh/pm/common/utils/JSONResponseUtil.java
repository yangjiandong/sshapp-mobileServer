package org.ssh.pm.common.utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.web.ServletUtils;

public class JSONResponseUtil {
    protected static Logger logger = LoggerFactory.getLogger(JSONResponseUtil.class.getName());

    /**
     * 响应生成Grid格式的JSON数据集 <br>
     * 'totalCount':Grid的JSONReader的totalProperty属性必须设置为"totalCount" <br>
     * 'rows':Grid的JSONReader的root属性必须设置为"rows"
     */
    public static void buildJSONDataResponse(HttpServletResponse response, String jsonString, Long total)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"totalCount\":" + total + ",\"rows\":");
        sb.append(jsonString);
        sb.append("}");

        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(sb.toString());
    }

    //采用json，指定延迟加载的字段
    public static void buildJSONDataResponseLazy(HttpServletResponse response, List<? extends Object> data, Long total, String... lazys)
            throws Exception {
        JsonConfig cfg = new JsonConfig();

        ArrayList<String>all = new ArrayList<String>();
        all.add("handler");
        all.add("hibernateLazyInitializer");
        for (String string : lazys) {
            all.add(string);
        }

        String []strArray = new String[2+lazys.length];
        all.toArray(strArray);
        cfg.setExcludes(strArray);
        JSONArray jsonArray = JSONArray.fromObject(data, cfg);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"totalCount\":" + total + ",\"rows\":");
        sb.append(jsonArray.toString());
        sb.append("}");
        String encoding = "UTF-8";
        boolean noCache = true;
        //response.setContentType("text/json; charset=UTF-8");
        response.setContentType(ServletUtils.JSON_TYPE + ";charset=" + encoding);
        if (noCache) {
            ServletUtils.setNoCacheHeader(response);
        }
        PrintWriter out = response.getWriter();
        out.write(sb.toString());
    }

    /**
     * 响应生成Grid格式的JSON数据集 <br>
     * 'totalCount':Grid的JSONReader的totalProperty属性必须设置为"totalCount" <br>
     * 'rows':Grid的JSONReader的root属性必须设置为"rows"
     */
    public static void buildJSONDataResponse(HttpServletResponse response, List<? extends Object> data, Long total)
            throws Exception {

        JSONArray jsonArray = JSONArray.fromObject(data);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"totalCount\":" + total + ",\"rows\":");
        sb.append(jsonArray.toString());
        sb.append("}");

        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        //logger.debug(sb.toString());

        out.write(sb.toString());
    }

    public static void buildCustomJSONDataResponseLzay(HttpServletResponse response, Map<String, ? extends Object> data,String... lazys)
            throws Exception {
        JsonConfig cfg = new JsonConfig();

        ArrayList<String>all = new ArrayList<String>();
        all.add("handler");
        all.add("hibernateLazyInitializer");
        for (String string : lazys) {
            all.add(string);
        }

        String []strArray = new String[2+lazys.length];
        all.toArray(strArray);
        cfg.setExcludes(strArray);
        JSONObject jsonObject = JSONObject.fromObject(data,cfg);
        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(jsonObject.toString());
    }

    /**
     * 响应生成Grid格式的JSON数据集 <br>
     * JSON对象的属性可以根据需要自定义设置，通过Map添加键值即可<br>
     * 如: Map<String,Object> map = new HashMap<String,Object>();<br>
     * map.put("totalCount",125);<br>
     * map.put("success",true);<br>
     * map.put("data",list);
     */
    public static void buildCustomJSONDataResponse(HttpServletResponse response, Map<String, ? extends Object> data)
            throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(data);
        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(jsonObject.toString());
    }

    /**
     * 响应非Grid形式的JSON数据集的请求
     */
    public static void buildJSONResponse(HttpServletResponse response, List<? extends Object> data) throws Exception {
        JSONArray jsonArray = JSONArray.fromObject(data);
        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(jsonArray.toString());
    }

    /**
     * 响应非Grid形式的JSON数据集的请求
     */
    public static void buildJSONObjectResponse(HttpServletResponse response, JSONObject data) throws Exception {
        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(data.toString());
    }
}
