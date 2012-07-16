package org.ssh.pm.query.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssh.pm.query.service.QueryService;

//http://192.168.1.112:8090/sshapp/query/query?type=mobile

@Controller
@RequestMapping("/query2")
public class QueryController {
    private static Logger logger = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    private QueryService queryService;

    @RequestMapping("/query")
    public @ResponseBody
    Map<String, Object> query(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String itemId = request.getParameter("itemId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //        List<ItemSource> data = queryService.query(itemId, startDate, endDate);

        //JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        System.out.println("query2/query,...");
        return map;
    }

}
