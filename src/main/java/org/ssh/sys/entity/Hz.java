package org.ssh.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;

@Entity
//@org.hibernate.annotations.Table(appliesTo = "T_HZK",
//    indexes = {
//        @Index(name="index1", columnNames={"column1", "column2"} ) }
//)
@Table(name = "T_HZK")
public class Hz implements Serializable{

    private static final long serialVersionUID = 2136790018031187846L;

    private Long id;
    private String hz;
    private String wb;
    private String py;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(length = 10)
    @Index(name="index1",columnNames={"hz"})
    //@Index(name="index1",columnNames={"hz2"}) 复合索引
    public String getHz() {
        return hz;
    }

    public void setHz(String hz) {
        this.hz = hz;
    }

    @Column(length = 10)
    public String getWb() {
        return wb;
    }

    public void setWb(String wb) {
        this.wb = wb;
    }

    @Column(length = 10)
    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

//    @Id
//    public Long getOid() {
//        return oid;
//    }
//
//    public void setOid(Long oid) {
//        this.oid = oid;
//    }
}
