package org.ssh.pm.common.webservice.rs.server.result;

import javax.xml.bind.annotation.XmlType;

import org.ssh.pm.common.webservice.rs.server.WsConstants;
import org.ssh.pm.common.webservice.rs.server.dto.HzDTO;

/**
 * GetHz方法的返回结果.
 * 
 */
@XmlType(name = "GetHzResult", namespace = WsConstants.NS)
public class GetHzResult extends WSResult {

    private HzDTO hz;

    public HzDTO getHz() {
        return hz;
    }

    public void setHz(HzDTO hz) {
        this.hz = hz;
    }
}
