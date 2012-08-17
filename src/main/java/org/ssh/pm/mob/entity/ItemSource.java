package org.ssh.pm.mob.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

//自动执行任务的数据源
@Entity
@Table(name = "Mob_ItemSource")
public class ItemSource implements Serializable {
    private static final long serialVersionUID = 8658702729921362894L;
    @ViewField
    private Long id;
    @ViewField
    private Long itemId;
    @ViewField(header = "指标名称")
    private String itemName;
    @ViewField(header = "存储过程名称")
    private String spName;
    @ViewField
    private String typeCode;//标记菜单来源,01:全院概况
    @ViewField
    private Long cronId;
    @ViewField
    private String cron;
    @ViewField(header = "执行方式")
    private String expression;

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "Mob_ItemSource", initialValue = 1, allocationSize = 1)
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Long getCronId() {
        return cronId;
    }

    public void setCronId(Long cronId) {
        this.cronId = cronId;
    }

}