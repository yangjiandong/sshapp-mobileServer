package org.ssh.pm.nurse.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

@Entity
@Table(name = "Mob_VitalSignItem")
public class VitalSignItem implements Serializable {

    private static final long serialVersionUID = -6232045729105276809L;
    @ViewField
    private Long id;
    @ViewField(header = "编号")
    private String code;
    @ViewField(header = "体征指标")
    private String name;
    @ViewField(header = "单位")
    private String unit;
    @ViewField
    private String typeCode;

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

}