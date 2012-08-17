package org.ssh.pm.mob.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

@Entity
@Table(name = "Mob_CronType")
public class CronType implements Serializable {

    private static final long serialVersionUID = 461139665300881019L;

    @ViewField
    private Long id;
    @ViewField(header = "自动任务执行方式 ")
    private String expression;
    @ViewField
    private String cron;

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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

}