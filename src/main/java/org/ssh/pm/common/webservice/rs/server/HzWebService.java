package org.ssh.pm.common.webservice.rs.server;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.ssh.pm.common.webservice.rs.server.result.GetAllHzResult;
import org.ssh.pm.common.webservice.rs.server.result.GetHzResult;

/**
 * JAX-WS2.0的WebService接口定义类.
 *
 */
@WebService(name = "HzService", targetNamespace = WsConstants.NS)
public interface HzWebService {
    /**
     * 显示所有Hz.
     */
    GetAllHzResult getAllHz();

    /**
     * 获取Hz.
     */
    GetHzResult getHz(@WebParam(name = "hz") String hz);
}
