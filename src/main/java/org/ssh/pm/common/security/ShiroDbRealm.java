package org.ssh.pm.common.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.ssh.sys.entity.User;
import org.ssh.sys.service.AccountManager;

public class ShiroDbRealm extends AuthorizingRealm {
    @Autowired
    private AccountManager accountManager;


    public ShiroDbRealm() {
        setCredentialsMatcher(new HashedCredentialsMatcher("SHA-1"));
    }

//    /**
//    * 认证回调函数,登录时调用.
//    */
//    protected AuthenticationInfo doGetAuthenticationInfo2(AuthenticationToken authcToken) throws AuthenticationException {
//        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
//        String loginName = token.getUsername();
//        if( loginName != null && !"".equals(loginName) ){
//            User user = accountManager.login(token.getUsername(),
//                            String.valueOf(token.getPassword()));
//
//            if( user != null )
//                return new SimpleAuthenticationInfo(
//                            user.getLoginName(),user.getPassword(), getName());
//        }
//        return null;
//    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        User user = accountManager.findUserByLoginName(token.getUsername());
        if (user != null) {
            return new SimpleAuthenticationInfo(user.getLoginName(), user.getPassword(), getName());
        } else {
            return null;
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String loginName = (String) principals.fromRealm(getName()).iterator().next();
        User user = accountManager.findUserByLoginName(loginName);
        //TODO
//        if( user != null && user.getRoles() != null ){
//            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//            for( Role each: user.getRoles() ){
//                    info.addRole(each.getName());
//                    info.addStringPermissions(each.getPermissionsAsString());
//            }
//            return info;
//        }
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole("common");
            info.addStringPermission("common-user");
            return info;
        } else {
            return null;
        }
    }

    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }
}
