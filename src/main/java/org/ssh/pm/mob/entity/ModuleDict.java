package org.ssh.pm.mob.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

@Entity
@Table(name = "Mob_ModuleDict")
public class ModuleDict implements Serializable {

    private static final long serialVersionUID = 5895490729381402477L;
    @ViewField(header = "编号")
    private String code;
    @ViewField(header = "模块名称 ")
    private String name;

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Id
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}