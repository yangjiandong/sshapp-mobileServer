package org.ssh.pm.approval.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

//用药审批

@Entity
@Table(name = "Mob_DrugApprovalData")
public class DrugApprovalData implements Serializable {

    private static final long serialVersionUID = 5683566828918362394L;
    @ViewField
    private Long id;
    @ViewField
    private Long appNo;//审批编号
    @ViewField
    private String patientId;//住院号
    @ViewField
    private String visitId;//就诊次数
    @ViewField
    private String patientName;//病人姓名
    @ViewField
    private String sex;//性别
    @ViewField
    private String age;//年龄
    @ViewField
    private String chargeType;//医保费别
    @ViewField
    private String bedNo;//床位号
    @ViewField
    private String deptCode;//所在病区代码
    @ViewField
    private String deptName;//所在病区
    @ViewField
    private String meddleType;//干预类型
    @ViewField
    private String diagnosis;//主要诊断
    @ViewField
    private String useReasion;//使用原因
    @ViewField
    private String drugCode;//药品代码
    @ViewField
    private String drugName;//药品名称
    @ViewField
    private String drugSpec;//药品规格
    @ViewField
    private String result;//审批结果,Y同意,N不同意
    @ViewField
    private String note;//备注
    @ViewField
    private String appDate;//申请时间
    @ViewField
    private String appWho;//申请人
    @ViewField
    private String appWhoCode;//申请人代码
    @ViewField
    private String receiveWho;//接收人
    @ViewField
    private String receiveWhoCode;//接收人代码
    @ViewField
    private String receiveDeptName;//接收科室
    @ViewField
    private String receiveDeptCode;//接收科室代码
    @ViewField
    private String dealDate;//审批时间
    @ViewField
    private String dealWho;//审批人
    @ViewField
    private String dealWhoCode;//审批人代码
    @ViewField
    private int state;//1未审核,2已审核未提交,3已提交

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

    public Long getAppNo() {
        return appNo;
    }

    public void setAppNo(Long appNo) {
        this.appNo = appNo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getUseReasion() {
        return useReasion;
    }

    public void setUseReasion(String useReasion) {
        this.useReasion = useReasion;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugSpec() {
        return drugSpec;
    }

    public void setDrugSpec(String drugSpec) {
        this.drugSpec = drugSpec;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMeddleType() {
        return meddleType;
    }

    public void setMeddleType(String meddleType) {
        this.meddleType = meddleType;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getDealWho() {
        return dealWho;
    }

    public void setDealWho(String dealWho) {
        this.dealWho = dealWho;
    }

    public String getDealWhoCode() {
        return dealWhoCode;
    }

    public void setDealWhoCode(String dealWhoCode) {
        this.dealWhoCode = dealWhoCode;
    }

    public String getAppWho() {
        return appWho;
    }

    public void setAppWho(String appWho) {
        this.appWho = appWho;
    }

    public String getAppWhoCode() {
        return appWhoCode;
    }

    public void setAppWhoCode(String appWhoCode) {
        this.appWhoCode = appWhoCode;
    }

    public String getReceiveWho() {
        return receiveWho;
    }

    public void setReceiveWho(String receiveWho) {
        this.receiveWho = receiveWho;
    }

    public String getReceiveWhoCode() {
        return receiveWhoCode;
    }

    public void setReceiveWhoCode(String receiveWhoCode) {
        this.receiveWhoCode = receiveWhoCode;
    }

    public String getReceiveDeptName() {
        return receiveDeptName;
    }

    public void setReceiveDeptName(String receiveDeptName) {
        this.receiveDeptName = receiveDeptName;
    }

    public String getReceiveDeptCode() {
        return receiveDeptCode;
    }

    public void setReceiveDeptCode(String receiveDeptCode) {
        this.receiveDeptCode = receiveDeptCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

}