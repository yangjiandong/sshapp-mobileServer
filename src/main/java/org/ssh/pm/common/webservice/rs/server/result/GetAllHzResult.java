package org.ssh.pm.common.webservice.rs.server.result;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.ssh.pm.common.webservice.rs.server.WsConstants;
import org.ssh.pm.common.webservice.rs.server.dto.HzDTO;

/**
 * GetAllHz方法的返回结果类型.
 *
 */
@XmlType(name = "GetAllHzResult", namespace = WsConstants.NS)
public class GetAllHzResult extends WSResult implements Serializable {
    private static final long serialVersionUID = -2363503575476130485L;
    private List<HzDTO> hzList;

	@XmlElementWrapper(name = "hzList")
	@XmlElement(name = "hz")
	public List<HzDTO> getHzList() {
		return hzList;
	}

	public void setHzList(List<HzDTO> hzList) {
		this.hzList = hzList;
	}
}
