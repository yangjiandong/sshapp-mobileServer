package org.ssh.pm.approval.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

//手术审批

@Entity
@Table(name = "Mob_OperationApprovalData")
public class OperationApprovalData implements Serializable {

    private static final long serialVersionUID = -3003945984880482170L;
    @ViewField
    private Long id;
    @ViewField
    private String patientId;//住院号
    @ViewField
    private String visitId;//就诊次数
    @ViewField
    private String operId;//手术编号
    @ViewField
    private String patientName;//病人姓名
    @ViewField
    private String appDate;//申请时间
    @ViewField
    private String sex;//性别
    @ViewField
    private String age;//年龄
    @ViewField
    private String bedNo;//床位号
    @ViewField
    private String deptCode;//所在病区代码
    @ViewField
    private String deptName;//所在病区
    @ViewField
    private String operationName;//手术名称
    @ViewField
    private String operationStep;//手术等级
    @ViewField
    private String checkOut;//病人费别
    @ViewField
    private String cut;//手术切口
    @ViewField
    private String diagnosis;//术前诊断
    @ViewField
    private String condition;//病情
    @ViewField
    private String operDate;//手术日期
    @ViewField
    private String operDeptCode;//手术室代码
    @ViewField
    private String operDeptName;//手术室
    @ViewField
    private String operRoom;//手术房间号
    @ViewField
    private String operAmount;//手术台次
    @ViewField
    private String isolation;//隔离标志
    @ViewField
    private String specialOperation;//特殊手术
    @ViewField
    private String operByDeptCode;//手术科室代码
    @ViewField
    private String operByDeptName;//手术科室
    @ViewField
    private String operDoctorCode;//手术医生代码
    @ViewField
    private String operDoctorName;//手术医生
    @ViewField
    private String assistant1;//助手1
    @ViewField
    private String assistant2;//助手2
    @ViewField
    private String assistant3;//助手3
    @ViewField
    private String assistant4;//助手4
    @ViewField
    private String narcosisDoctorCode;//麻醉医生代码
    @ViewField
    private String narcosisDoctorName;//麻醉医生
    @ViewField
    private String narcosisMethod;//麻醉方法
    @ViewField
    private String narcosisMethodCode;//麻醉方法代码
    @ViewField
    private String operPrepare;//术前准备
    @ViewField
    private String result;//审批结果,Y同意,N不同意
    @ViewField
    private String note;//备注
    @ViewField
    private String dealDate;//审批时间
    @ViewField
    private String dealWho;//审批人
    @ViewField
    private String dealWhoCode;//审批人代码
    @ViewField
    private int state;//1未审核,2已审核未提交,3已提交

    @ViewField
    private String appWho;//申请人
    @ViewField
    private String appWhoCode;//申请人代码
    @ViewField
    private String receiveWho;//接收人
    @ViewField
    private String receiveWhoCode;//接收人代码

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

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationStep() {
        return operationStep;
    }

    public void setOperationStep(String operationStep) {
        this.operationStep = operationStep;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getOperDeptCode() {
        return operDeptCode;
    }

    public void setOperDeptCode(String operDeptCode) {
        this.operDeptCode = operDeptCode;
    }

    public String getOperDeptName() {
        return operDeptName;
    }

    public void setOperDeptName(String operDeptName) {
        this.operDeptName = operDeptName;
    }

    public String getOperRoom() {
        return operRoom;
    }

    public void setOperRoom(String operRoom) {
        this.operRoom = operRoom;
    }

    public String getOperAmount() {
        return operAmount;
    }

    public void setOperAmount(String operAmount) {
        this.operAmount = operAmount;
    }

    public String getIsolation() {
        return isolation;
    }

    public void setIsolation(String isolation) {
        this.isolation = isolation;
    }

    public String getSpecialOperation() {
        return specialOperation;
    }

    public void setSpecialOperation(String specialOperation) {
        this.specialOperation = specialOperation;
    }

    public String getOperByDeptCode() {
        return operByDeptCode;
    }

    public void setOperByDeptCode(String operByDeptCode) {
        this.operByDeptCode = operByDeptCode;
    }

    public String getOperByDeptName() {
        return operByDeptName;
    }

    public void setOperByDeptName(String operByDeptName) {
        this.operByDeptName = operByDeptName;
    }

    public String getOperDoctorCode() {
        return operDoctorCode;
    }

    public void setOperDoctorCode(String operDoctorCode) {
        this.operDoctorCode = operDoctorCode;
    }

    public String getOperDoctorName() {
        return operDoctorName;
    }

    public void setOperDoctorName(String operDoctorName) {
        this.operDoctorName = operDoctorName;
    }

    public String getAssistant1() {
        return assistant1;
    }

    public void setAssistant1(String assistant1) {
        this.assistant1 = assistant1;
    }

    public String getAssistant2() {
        return assistant2;
    }

    public void setAssistant2(String assistant2) {
        this.assistant2 = assistant2;
    }

    public String getAssistant3() {
        return assistant3;
    }

    public void setAssistant3(String assistant3) {
        this.assistant3 = assistant3;
    }

    public String getAssistant4() {
        return assistant4;
    }

    public void setAssistant4(String assistant4) {
        this.assistant4 = assistant4;
    }

    public String getNarcosisDoctorCode() {
        return narcosisDoctorCode;
    }

    public void setNarcosisDoctorCode(String narcosisDoctorCode) {
        this.narcosisDoctorCode = narcosisDoctorCode;
    }

    public String getNarcosisDoctorName() {
        return narcosisDoctorName;
    }

    public void setNarcosisDoctorName(String narcosisDoctorName) {
        this.narcosisDoctorName = narcosisDoctorName;
    }

    public String getNarcosisMethod() {
        return narcosisMethod;
    }

    public void setNarcosisMethod(String narcosisMethod) {
        this.narcosisMethod = narcosisMethod;
    }

    public String getNarcosisMethodCode() {
        return narcosisMethodCode;
    }

    public void setNarcosisMethodCode(String narcosisMethodCode) {
        this.narcosisMethodCode = narcosisMethodCode;
    }

    public String getOperPrepare() {
        return operPrepare;
    }

    public void setOperPrepare(String operPrepare) {
        this.operPrepare = operPrepare;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

}