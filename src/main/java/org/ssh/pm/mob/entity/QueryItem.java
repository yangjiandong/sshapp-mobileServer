package org.ssh.pm.mob.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

//全院概括指标
@Entity
@Table(name = "Mob_QueryItem")
public class QueryItem implements Serializable {

    private static final long serialVersionUID = 461139665300881018L;
    @ViewField(header = "编号")
    private Long id;
    @ViewField(header = "指标名称")
    private String itemName;
    @ViewField
    private Long parentId;
    @ViewField
    private String isleaf;
    @ViewField
    private Long codeLevel;

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }

    public Long getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(Long codeLevel) {
        this.codeLevel = codeLevel;
    }

}