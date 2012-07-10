package org.ssh.data;

import org.springside.modules.test.utils.DataUtils;
import org.ssh.sys.entity.Role;
import org.ssh.sys.entity.User;

public class UserData {
    
    public String my_method(){
        return "hello";
    }
    
    public String my_field = "other";
    
    public static Long getUserId() {
        //return (String) new UIDGenerator().generate(null, null);
        return 1L;
    }

    public static User getRandomUser() {
        String userName = DataUtils.randomName("User");

        User user = new User();
        user.setLoginName(userName);
        user.setName(userName);
        user.setPassword("123456");
        user.setEmail(userName + "@springside.org.cn");
        user.setUpdatePwd("1");

        return user;
    }

    public static User getRandomUserWithAdminRole() {
        User user = UserData.getRandomUser();
        Role adminRole = UserData.getAdminRole();
        user.getRoleList().add(adminRole);
        return user;
    }

    public static Role getAdminRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        return role;
    }
    
    public static void main(String[] args) throws Exception{
        Object object = new UserData();
        String method="my_method";
        String field = "my_field";
        
        String hello =(String) object.getClass().getMethod(method).invoke(object);
        
        // Dynamically access a field
        object.getClass().getField(field).get(object);
        
    }
}
