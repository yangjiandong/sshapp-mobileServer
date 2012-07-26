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
import org.ssh.pm.orm.hibernate.AuditableEntity;

/**
 * 移动端版本升级记录
 *
 */
@Entity
@Table(name = "Mob_Version")
public class MobVersion extends AuditableEntity implements Serializable {

    private static final long serialVersionUID = 5652022567420238293L;

    @ViewField(header = "版本", width = 200)
    private String version;
    @ViewField(header = "文件名", width = 250)
    private String fileName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Column(nullable = false)
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Column(nullable = false)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
