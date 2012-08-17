package org.ssh.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

//后台迁移
@Entity
@Table(name = "T_Migration")
public class Migration implements Serializable {

    public static final String SQLTYPE_EXEC = "EXEC";
    public static final String SQLTYPE_SQL = "SQL";
    public static final String SQLTYPE_SP = "SP";

    private static final long serialVersionUID = 2136790018031187866L;

    @ViewField()
    private Long id;
    @ViewField(header = "更新说明", width = 150)
    private String name;
    @ViewField(header = "详细说明", width = 250)
    private String descrip;
    private String sqlType;
    @ViewField(header = "状态", width = 50)
    private String status;
    @ViewField(header = "创建时间", width = 150)
    private String createTime;
    @ViewField(header = "创建人", width = 100)
    private String createBy;
    @ViewField(header = "更新时间", width = 150)
    private String runTime;
    @ViewField(header = "更新人", width = 100)
    private String runBy;
    @ViewField(header = "更新操作内容", width = 300)
    private String sqlS;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Column(length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 10)
    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    //0-未做,1-完成
    @Column(length = 1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(length = 50)
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(length = 50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(length = 50)
    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    @Column(length = 50)
    public String getRunBy() {
        return runBy;
    }

    public void setRunBy(String runBy) {
        this.runBy = runBy;
    }

    @Column(length = 400)
    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    @Column(length = 4000)
    public String getSqlS() {
        return sqlS;
    }

    public void setSqlS(String sqlS) {
        this.sqlS = sqlS;
    }

}
