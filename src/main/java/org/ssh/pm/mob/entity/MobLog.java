package org.ssh.pm.mob.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

//用户操作mob日志,如出错异常
@Entity
@Table(name = "Mob_Log")
public class MobLog implements Serializable {

    private static final long serialVersionUID = -7991345612891933364L;
    @ViewField
    private Long id;
    @ViewField(header = "编号")
    private Long userId;
    @ViewField
    private String netIp;
    @ViewField
    private String createTime;
    @ViewField
    private String event;
    @ViewField
    private String note;
    @ViewField
    private String type;
    @ViewField
    private String from;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(length = 100)
    public String getNetIp() {
        return netIp;
    }

    public void setNetIp(String netIp) {
        this.netIp = netIp;
    }

    @Column(length = 50)
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(length = 100)
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Column(length = 10)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(length = 100, name = "froms")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}