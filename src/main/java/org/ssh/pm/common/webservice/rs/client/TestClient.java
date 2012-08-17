package org.ssh.pm.common.webservice.rs.client;

import org.ssh.pm.common.webservice.rs.dto.UserDTO;

public class TestClient {
    public static void main(String[] args) {
        final String BASE_URL = "http://localhost:8090/sshapp";
        UserResourceClient client;
        client = new UserResourceClient();
        client.setBaseUrl(BASE_URL + "/rs");

        //UserDTO admin = client.searchUserReturnJson("Admin");
        String html = client.searchUserReturnHtml("管理员");
        //System.out.println(admin.getName());
        System.out.println(html);

        UserDTO json = client.searchUserReturnJson("管理员");
        System.out.println(json);

        String simpleTest = client.simpleTest("");
        System.out.println(simpleTest);
    }
}
