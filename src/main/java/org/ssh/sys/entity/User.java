package org.ssh.sys.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springside.modules.orm.grid.ViewField;
import org.ssh.pm.orm.hibernate.AuditableEntity;

import com.google.common.collect.Lists;

/**
 * 用户.
 *
 */
@Entity
@Table(name = "T_USERS")
public class User extends AuditableEntity implements Serializable {
    private static final long serialVersionUID = -6692220655220951663L;

    @ViewField(header = "登录用户", width = 120)
    private String loginName;
    @ViewField
    private String password;
    @ViewField(header = "姓名", width = 120)
    private String name;
    @ViewField(header = "状态", width = 120)
    private String status;
    @ViewField(header = "邮箱", width = 180)
    private String email;
    @ViewField
    private Integer version;
    // 用于传递用户选中的数据库code
    @ViewField
    private String dbCodes;

    //密码状态,0必须修改1已修改
    private String updatePwd;
    @ViewField
    private String telNumber;
    @ViewField
    private String mobileNo;

    //his信息
    private String userNo;
    private String departNo;
    private String departName;
    private String title;
    private String job;

    //有序的关联对象集合
    private List<Role> roleList = Lists.newArrayList();

    private List<PartDB> partDBList = Lists.newArrayList();

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "T_USERS", initialValue = 9000, allocationSize = 1)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    //Hibernate自动维护的Version字段
    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(nullable = false, unique = true, length = 20)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Column(length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(length = 10)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //多对多定义
    @ManyToMany
    //中间表定义,表名采用默认命名规则
    @JoinTable(name = "T_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    //Fecth策略定义
    @Fetch(FetchMode.SUBSELECT)
    //集合按id排序
    @OrderBy("id ASC")
    public List<Role> getRoleList() {
        return roleList;
    }

    //@Fetch(FetchMode.JOIN) 会使用left join查询  只产生一条sql语句
    //@Fetch(FetchMode.SELECT)   会产生N+1条sql语句
    //@Fetch(FetchMode.SUBSELECT)  产生两条sql语句 第二条语句使用id in (.....)查询出所有关联的数据
    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    //    @Transient
    //    @JsonIgnore
    //    public String getRoleNames() {
    //        return ConvertUtils.convertElementPropertyToString(roleList, "name", ", ");
    //    }

    //多对多定义
    @ManyToMany
    //中间表定义,表名采用默认命名规则
    @JoinTable(name = "T_USER_DB", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "DBCODE") })
    //Fecth策略定义
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("dbcode ASC")
    public List<PartDB> getPartDBList() {
        return partDBList;
    }

    public void setPartDBList(List<PartDB> partDBList) {
        this.partDBList = partDBList;
    }

    //    @Transient
    //    @JsonIgnore
    //    public String getDBNames() {
    //        return ConvertUtils.convertElementPropertyToString(partDBList, "dbname", ", ");
    //    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getUserName() {
        return name.equals("") ? loginName : name;
    }

    @Transient
    public boolean isTransient() {
        return this.id == null;
    }

    @Transient
    public String getDbCodes() {
        return dbCodes;
    }

    public void setDbCodes(String dbCodes) {
        this.dbCodes = dbCodes;
    }

    @Column(length = 1, nullable = false)
    public String getUpdatePwd() {
        return updatePwd;
    }

    public void setUpdatePwd(String updatePwd) {
        this.updatePwd = updatePwd;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getDepartNo() {
        return departNo;
    }

    public void setDepartNo(String departNo) {
        this.departNo = departNo;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
