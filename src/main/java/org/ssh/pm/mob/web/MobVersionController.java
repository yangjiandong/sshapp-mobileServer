package org.ssh.pm.mob.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.ssh.pm.common.utils.AppUtil;
import org.ssh.pm.common.web.FileUploadBean;
import org.ssh.pm.mob.entity.MobVersion;
import org.ssh.pm.mob.service.MobVersionService;

@Controller
@RequestMapping("/mob_version")
public class MobVersionController {
    private static Logger logger = LoggerFactory.getLogger(MobVersionController.class);

    @Autowired
    MobVersionService mobVersionService;

    @RequestMapping("/query")
    public @ResponseBody
    Map<String, Object> query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        Page<MobVersion> page = new Page<MobVersion>(request);
        String orderColumn = request.getParameter("sort");
        String dir = request.getParameter("dir");
        if (StringUtils.isBlank(orderColumn)) {
            page.setOrderBy("version");
            page.setOrder(Page.DESC);

        } else {
            page.setOrderBy(orderColumn);
            page.setOrder(dir);
        }
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();

        Page<MobVersion> data = mobVersionService.query(page, filters);
        map = AppUtil.buildJSONDataResponse(data.getResult(), (long) data.getTotalCount());
        return map;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/deploy")
    public @ResponseBody
    String deploy(HttpServletRequest request, HttpServletResponse response, FileUploadBean uploadItem,
            BindingResult result) throws Exception {

        String UPLOAD_SUCCESS = "{success:true,mess:'文件上传成功!'}";
        String UPLOAD_FAILURE = "{success:false,mess:'文件上传失败!'}";

        response.setContentType("text/json; charset=UTF-8");
        if (!ServletFileUpload.isMultipartContent(request)) {
            return UPLOAD_FAILURE.toString();
        }

        try {
            //
            //sshapp-mobileapp_1_b1_2012070823.apk
            String fileName = uploadItem.getFile().getFileItem().getName();
            MobVersion f = mobVersionService.findByUnique("fileName", fileName);
            if (f != null) {
                logger.error("文件上传失败!该版本文件已存在。", fileName);
                return "{success:false,mess:'文件上传失败!该版本文件已存在。'}";
            }
            final String ext = "apk";
            if (!FilenameUtils.getExtension(fileName).equals(ext)){
                logger.error("文件上传失败!文件类型不对", fileName);
                return "{success:false,mess:'文件上传失败!文件类型不对。'}";
            }

            String temp = fileName.substring(0, fileName.length() - ext.length() - 1);
            String version = temp.substring(temp.indexOf("_") + 1);

            String path = null;
            Map map = System.getenv();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getKey().equals("SSH_CONFIG_DIR")) //读取SSH_CONFIG_DIR的环境变量
                    path = entry.getValue().toString();

            }

            path = path + "/upload/";//+ upTime.getYearMonth();
            if (result.hasErrors()) {
                return UPLOAD_FAILURE.toString();
            }

            if (uploadItem.getFile().getSize() > 0) {
                File file = new File(path + "/" + uploadItem.getFile().getFileItem().getName()); //新建一个文件
                try {
                    uploadItem.getFile().getFileItem().write(file); //将上传的文件写入新建的文件中
                } catch (Exception e) {
                    logger.error("", e);
                    e.printStackTrace();
                    return UPLOAD_FAILURE.toString();
                }

                MobVersion mobversion = new MobVersion();
                mobversion.setVersion(version);
                mobversion.setFileName(fileName);
                mobVersionService.save(mobversion);
            }
        } catch (Exception e) {
            logger.error("", e);
            return UPLOAD_FAILURE.toString();
        }
        return UPLOAD_SUCCESS.toString();

    }

}
