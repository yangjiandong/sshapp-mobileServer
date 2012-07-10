package org.ssh.pm.orm.hibernate.customId;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 所有的实体类的父类，集成了一些公用属性
 *
 * @author XXX
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "custgenerator")
    @GenericGenerator(strategy = "pool", name = "custgenerator", parameters={@Parameter(name="poolsize", value="100")})
    @Column(length = 20)
    private String id;

    /**
     * 数据版本号
     */
    @Version
    private Long version;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
