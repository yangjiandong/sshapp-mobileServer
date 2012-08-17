package org.ssh.pm.nurse.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

//测量类别
@Entity
@Table(name = "Mob_MeasureType")
public class MeasureType implements Serializable {

    private static final long serialVersionUID = 9092644027891064536L;
    @ViewField
    private Long id;
    @ViewField(header = "编号")
    private String code;
    @ViewField(header = "测量类型")
    private String name;

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

}