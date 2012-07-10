package org.ssh.pm.common.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.utils.JsonViewUtil;
import org.springside.modules.utils.leona.JsonUtils;
import org.ssh.sys.entity.Hz;
import org.ssh.sys.service.HzService;

@Controller
@RequestMapping("/book")
public class BookController {
    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private HzService hzService;

    @RequestMapping(value = "/getBooks")
    public void showBooks(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JsonViewUtil.buildJSONDataResponse(response, "test", (long) 1);
    }

    @RequestMapping(value = "/getHz")
    public void showHz(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("test memcahced...");
        long start = System.currentTimeMillis();

        Map<String, String> allBooks = this.hzService.getMemo("测试");
        JSONArray jsonArray = JSONArray.fromObject(allBooks);

        logger.info(" cache 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        JsonViewUtil.buildJSONDataResponse(response, jsonArray.toString(), (long) 1);
    }

    @RequestMapping(value = "/getAllHz")
    public ModelAndView showAllHz(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("test method memcahced...");
        long start = System.currentTimeMillis();

        List<Hz> books = hzService.getHzsOnMethodCache();

        logger.info(" method cache 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        return JsonViewUtil.getModelMap(books);
    }

    //去掉不需要生成json人内容
    @RequestMapping(value = "/getBooks33", method = RequestMethod.GET)
    public void showBooks33(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
//        //PdfGenerator manager = (PdfGenerator) SpringContextHolder.getBean("pdfGenerator");
//        logger.info("table cache list...");
//
//        long start = System.currentTimeMillis();
//
//        //        List<Book> books = bookService.getBooks2();
//        //        logger.info(" table cache 执行共计:" + (System.currentTimeMillis() - start) + " ms");
//        List<Category> books = categoryService.getBooks3();
//        logger.info(" 执行共计:" + (System.currentTimeMillis() - start)
//            + " ms");
//
//        Bean bean = new Bean(true, this.pdfGenerator.pdfFor(), books);
//        //避免contact 生成json,会报错
//        JsonUtils.write(bean, response.getWriter(),
//            new String[] {"hibernateLazyInitializer", "handler", "checked"},
//            "yyyy.MM.dd");
//
//        //{"info":[{"edition":10,"isbn":"comto ok","oid":10,"pages":200,"published":"AM","title":"goto American"},{"edition":10,"isbn":"omoo","oid":11,"pages":2000,"published":"AM","title":"计划生育"},{"edition":910,"isbn":"comtosadfad ok","oid":12,"pages":5300,"published":"AM","title":"同要有 面goto American"}],"msg":"已经登陆","success":true}
    }

}
