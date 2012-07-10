package org.ssh.pm.sms.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//
@Entity
@Table(name = "sms_checkLog")
public class CheckLog {
    private Long id;
    private int type;
    private int result;
    private Date date;
    private String itemid;
    private Date checkdate;
    private String visitid;
    private String patientid;
    private String checknote;
    private String name;
    private String orderid;
    private String doctor;
    private String checker;
    private String checkerid;
    private String checkDept;
    private String auxinfo1;
    private String auxinfo2;
    private String auxinfo3;
    private String auxinfo4;
    private String label1;
    private String label2;
    private String label3;
    private String label4;
    private String label5;
    private String label6;
    private String label7;
    private String label8;
    private String label9;
    private String label10;
    private String label11;
    private String label12;
    private String label13;
    private String label14;
    private String label15;
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;
    private String field6;
    private String field7;
    private String field8;
    private String field9;
    private String field10;
    private String field11;
    private String field12;
    private String field13;
    private String field14;
    private String field15;
    private String typename;
    private int smssent;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setVisitid(String visitid) {
        this.visitid = visitid;
    }

    public String getVisitid() {
        return visitid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getChecker() {
        return checker;
    }

    public void setAuxinfo1(String auxinfo1) {
        this.auxinfo1 = auxinfo1;
    }

    public String getAuxinfo1() {
        return auxinfo1;
    }

    public void setAuxinfo2(String auxinfo2) {
        this.auxinfo2 = auxinfo2;
    }

    public String getAuxinfo2() {
        return auxinfo2;
    }

    public void setAuxinfo3(String auxinfo3) {
        this.auxinfo3 = auxinfo3;
    }

    public String getAuxinfo3() {
        return auxinfo3;
    }

    public void setAuxinfo4(String auxinfo4) {
        this.auxinfo4 = auxinfo4;
    }

    public String getAuxinfo4() {
        return auxinfo4;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }

    public String getLabel3() {
        return label3;
    }

    public void setLabel4(String label4) {
        this.label4 = label4;
    }

    public String getLabel4() {
        return label4;
    }

    public void setLabel5(String label5) {
        this.label5 = label5;
    }

    public String getLabel5() {
        return label5;
    }

    public void setLabel6(String label6) {
        this.label6 = label6;
    }

    public String getLabel6() {
        return label6;
    }

    public void setLabel7(String label7) {
        this.label7 = label7;
    }

    public String getLabel7() {
        return label7;
    }

    public void setLabel8(String label8) {
        this.label8 = label8;
    }

    public String getLabel8() {
        return label8;
    }

    public void setLabel9(String label9) {
        this.label9 = label9;
    }

    public String getLabel9() {
        return label9;
    }

    public void setLabel10(String label10) {
        this.label10 = label10;
    }

    public String getLabel10() {
        return label10;
    }

    public void setLabel11(String label11) {
        this.label11 = label11;
    }

    public String getLabel11() {
        return label11;
    }

    public void setLabel12(String label12) {
        this.label12 = label12;
    }

    public String getLabel12() {
        return label12;
    }

    public void setLabel13(String label13) {
        this.label13 = label13;
    }

    public String getLabel13() {
        return label13;
    }

    public void setLabel14(String label14) {
        this.label14 = label14;
    }

    public String getLabel14() {
        return label14;
    }

    public void setLabel15(String label15) {
        this.label15 = label15;
    }

    public String getLabel15() {
        return label15;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField1() {
        return field1;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField2() {
        return field2;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField3() {
        return field3;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField4() {
        return field4;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField5() {
        return field5;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField6() {
        return field6;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getField7() {
        return field7;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField8() {
        return field8;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getField9() {
        return field9;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    public String getField10() {
        return field10;
    }

    public void setField11(String field11) {
        this.field11 = field11;
    }

    public String getField11() {
        return field11;
    }

    public void setField12(String field12) {
        this.field12 = field12;
    }

    public String getField12() {
        return field12;
    }

    public void setField13(String field13) {
        this.field13 = field13;
    }

    public String getField13() {
        return field13;
    }

    public void setField14(String field14) {
        this.field14 = field14;
    }

    public String getField14() {
        return field14;
    }

    public void setField15(String field15) {
        this.field15 = field15;
    }

    public String getField15() {
        return field15;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTypename() {
        return typename;
    }

    public void setSmssent(int smssent) {
        this.smssent = smssent;
    }

    public int getSmssent() {
        return smssent;
    }

    public void setCheckerid(String checkerid) {
        this.checkerid = checkerid;
    }

    public String getCheckerid() {
        return checkerid;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public Date getCheckdate() {
        return checkdate;
    }

    public void setChecknote(String checknote) {
        this.checknote = checknote;
    }

    public String getChecknote() {
        return checknote;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setCheckDept(String checkDept) {
        this.checkDept = checkDept;
    }

    public String getCheckDept() {
        return checkDept;
    }
}
