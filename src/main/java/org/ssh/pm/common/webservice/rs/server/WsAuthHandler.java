package org.ssh.pm.common.webservice.rs.server;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.cxf.interceptor.Fault;
//import org.apache.ws.security.WSPasswordCallback;
import org.apache.xmlbeans.impl.soap.SOAPException;

public class WsAuthHandler {
//implements CallbackHandler {

//    public void handle(Callback[] callbacks) throws IOException,
//            UnsupportedCallbackException {
//        for (int i = 0; i < callbacks.length; i++) {
//            WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
//            String identifier = pc.getIdentifier();
//            int usage = pc.getUsage();
//            if (usage == WSPasswordCallback.USERNAME_TOKEN) {// 密钥方式USERNAME_TOKEN
//                // username token pwd...
//                // ▲这里的值必须和客户端设的值相同,从cxf2.4.x后校验方式改为cxf内部实现校验，不必自己比较password是否相同
//                // 请参考：http://cxf.apache.org/docs/24-migration-guide.html的Runtime
//                // Changes片段
//                pc.setPassword("testPassword");// ▲【这里非常重要】▲
//                // ▲PS 如果和客户端不同将抛出org.apache.ws.security.WSSecurityException:
//                // The
//                // security token could not be authenticated or
//                // authorized异常，服务端会认为客户端为非法调用
//            } else if (usage == WSPasswordCallback.SIGNATURE) {// 密钥方式SIGNATURE
//                // set the password for client's keystore.keyPassword
//                // ▲这里的值必须和客户端设的值相同,从cxf2.4.x后校验方式改为cxf内部实现校验，不必自己比较password是否相同;
//                // 请参考：http://cxf.apache.org/docs/24-migration-guide.html的Runtime
//                // Changes片段
//                pc.setPassword("testPassword");// //▲【这里非常重要】▲
//                // ▲PS:如果和客户端不同将抛出org.apache.ws.security.WSSecurityException:The
//                // security token could not be authenticated or
//                // authorized异常，服务端会认为客户端为非法调用
//            }
//            if ("admin".equals(identifier)) {// 从cxf2.4.x后校验方式改为cxf内部实现校验，不必自己比较password是否相同
//                System.out.println("----- " + identifier + " 验证通过，放行"
//                        + " -----");
//            } else {
//                System.out.println("----- " + identifier
//                        + " 验证不通过，抛出异常，停止执行任何操作" + " -----");
//                SOAPException soapExc = new SOAPException("WsAuthHandler:非法用户！");
//                throw new Fault(soapExc);
//            }
//        }
//    }
}

