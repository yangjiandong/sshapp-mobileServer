package org.ssh.tool.json;


import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ssh.sys.entity.User;

public class JacksonExample {
    public static void main(String[] args) {

    User user = new User();
    user.setId(99L);
    user.setLoginName("test");
    user.setName("张生");
    ObjectMapper mapper = new ObjectMapper();

    try {
         // convert user object to json string, and save to a file
        mapper.writeValue(new File("c:\\user.json"), user);
         // display to console
        System.out.println(mapper.defaultPrettyPrintingWriter().writeValueAsString(user));
     } catch (JsonGenerationException e) {
         e.printStackTrace();

    } catch (JsonMappingException e) {
         e.printStackTrace();

    } catch (IOException e) {
        e.printStackTrace();
     }

  }
}

