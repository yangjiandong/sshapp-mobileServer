package org.ssh.pm.approval.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

//审批备注信息
@Entity
@Table(name = "Mob_ApprovalNote")
public class ApprovalNote implements Serializable {

    private static final long serialVersionUID = 2105454444578149569L;
    @ViewField
    private Long id;
    @ViewField(header = "编号")
    private String code;
    @ViewField(header = "备注内容", width = 400)
    private String name;
    private String typeCode;//1用药,2手术

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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}