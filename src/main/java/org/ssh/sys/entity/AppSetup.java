package org.ssh.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

/**
 * 系统参数设置
 */
@Entity
@Table(name = "T_APPSETUP")
public class AppSetup {

    @ViewField(header = "参数代码")
    private String setupCode;

    @ViewField(header = "参数值")
    private String setupValue;

    @ViewField(header = "备注")
    private String descrip;

    @Id
    @Column(nullable = false, length = 50)
    public String getSetupCode() {
        return setupCode;
    }

    public void setSetupCode(String setupCode) {
        this.setupCode = setupCode;
    }

    @Column(length = 50)
    public String getSetupValue() {
        return setupValue;
    }

    public void setSetupValue(String setupValue) {
        this.setupValue = setupValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Column(length = 400)
    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}
