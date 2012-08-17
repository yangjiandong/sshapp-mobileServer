package org.ssh.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;

/**
 * 菜单资源
 */
@Entity
@Table(name = "T_MyResources")
public class MyResource {

    private Long id;
    private Long expuid;
    private Long resourceId;
    private String text;
    private String url;
    private int codeLevel;
    private Long orderNo; // 资源排序字段
    private boolean leaf;
    private String state;
    private Long parentId;
    private String note;
    private String resourceType;
    //
    private String iconCls;
    //
    private String type; //菜单类型，系统预制01，自定义02
    //模块
    private Long mcode;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "T_MyResources", initialValue = 9000, allocationSize = 1)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Index(name = "expindex", columnNames = "expuid")
    public Long getExpuid() {
        return expuid;
    }

    public void setExpuid(Long expuid) {
        this.expuid = expuid;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    @Column(length = 200)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceType() {
        return resourceType;
    }

    @Transient
    public boolean isTransient() {
        return this.resourceId == null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Column(nullable = false, length = 50)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    @Column(length = 1, nullable = false)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(length = 10)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(int codeLevel) {
        this.codeLevel = codeLevel;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getMcode() {
        return mcode;
    }

    public void setMcode(Long mcode) {
        this.mcode = mcode;
    }
}