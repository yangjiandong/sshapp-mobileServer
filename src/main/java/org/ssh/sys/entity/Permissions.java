package org.ssh.sys.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

@Entity
@Table(name = "T_Permissions")
public class Permissions implements Serializable {
    private static final long serialVersionUID = -4471066536960096119L;

    @ViewField(header = "权限")
    private String name;
    @ViewField(header = "描述")
    private String descrip;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}
