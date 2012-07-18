package org.ssh.pm.nurse.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.modules.orm.grid.ViewField;

@Entity
@Table(name = "Mob_VitalSignData")
public class VitalSignData implements Serializable {

    private static final long serialVersionUID = -8373297171260457840L;
    @ViewField
    private Long id;
    @ViewField
    private Long userId;
    @ViewField
    private String patientId;//住院号
    @ViewField
    private String visitId;//就诊次数
    @ViewField
    private String addDate;//保存日期
    @ViewField
    private String timePoint;//时间点
    @ViewField
    private String timeCode;//时间点编号
    @ViewField
    private String itemCode;//指标编号
    @ViewField
    private String itemName;//指标
    @ViewField
    private String value1;//一日一次指标的数值
    @ViewField
    private String unit;//单位
    @ViewField
    private String value2;//一日多次指标的数值
    @ViewField
    private String measureTypeCode;//测量类别编号
    @ViewField
    private String state;//标记是否进行过保存,Y则提交给his

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

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getMeasureTypeCode() {
        return measureTypeCode;
    }

    public void setMeasureTypeCode(String measureTypeCode) {
        this.measureTypeCode = measureTypeCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}