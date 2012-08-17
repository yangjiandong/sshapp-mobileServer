package org.ssh.pm.common.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/uploadFile")
public class FileUploadController {
    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);


    @RequestMapping(value="/create", method = RequestMethod.POST)
    public @ResponseBody
    String create(FileUploadBean uploadItem, BindingResult result) {

        ExtJSFormResult extjsFormResult = new ExtJSFormResult();

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                //System.err.println("Error: " + error.getCode() + " - " + error.getDefaultMessage());
                logger.error("上载文件出错", error);
            }

            //set extjs return - error
            extjsFormResult.setSuccess(false);
            return extjsFormResult.toString();
        }

        // Some type of file processing...
        logger.debug("-------------------------------------------");
        logger.debug("Test upload: " + uploadItem.getFile().getOriginalFilename());
        logger.debug("-------------------------------------------");
        if (uploadItem.getFile().getSize() > 0) {
            try {
                String path = System.getenv("SSH_CONFIG_DIR");
                path += "//upload";
                File uploadedFile = new File(path + "/"+ uploadItem.getFile().getOriginalFilename());
                FileCopyUtils.copy(uploadItem.getFile().getBytes(), uploadedFile);
                //SaveFileFromInputStream(uploadItem.getFile().getInputStream(), path, uploadItem.getFile()
                //        .getOriginalFilename());
            } catch (IOException e) {
                logger.error("上载文件出错", e);
                //System.out.println(e.getMessage());
                return null;
            }
        }
        //set extjs return - sucsess
        extjsFormResult.setSuccess(true);

        return extjsFormResult.toString();
    }

    /* **保存文件

       * @param stream
       * @param path
       * @param filename
       * @throws IOException
       */
    public void SaveFileFromInputStream(InputStream stream, String path, String filename) throws IOException {
        FileOutputStream fs = new FileOutputStream(path + "/" + filename);
        byte[] buffer = new byte[1024 * 1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread = stream.read()) != -1) {
            bytesum += byteread;

            fs.write(buffer, 0, byteread);
            fs.flush();

        }
        fs.close();
        stream.close();
    }

}