package org.ssh.pm.common.webservice.rs.server.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlType(name = "Hz", namespace = org.ssh.pm.common.webservice.rs.server.WsConstants.NS)
public class HzDTO implements Serializable {
    private static final long serialVersionUID = -8582569322029464902L;

    private Long id;
    private String hz;
    private String wb;
    private String py;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHz() {
        return hz;
    }

    public void setHz(String hz) {
        this.hz = hz;
    }

    public String getWb() {
        return wb;
    }

    public void setWb(String wb) {
        this.wb = wb;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
