package org.ssh.pm.common.mapping.other;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.ssh.pm.common.mapping.xml.User;

@XmlRootElement
public class ListBean {
    private String name;
    private List list;

    @XmlElements({
        @XmlElement(name = "user", type = User.class)
    })
    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}