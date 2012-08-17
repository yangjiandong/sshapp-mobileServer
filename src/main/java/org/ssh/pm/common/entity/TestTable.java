package org.ssh.pm.common.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.ssh.pm.orm.hibernate.customId.BaseEntity;

@Entity
@Table(name = "Test_Table")
public class TestTable extends BaseEntity{

    private static final long serialVersionUID = 6147844903175016059L;
    private String no;
    private String name;
    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
