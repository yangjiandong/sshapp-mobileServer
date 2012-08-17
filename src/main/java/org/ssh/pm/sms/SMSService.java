package org.ssh.pm.sms;

import org.ssh.pm.sms.SMS.SMSReportStruct;

import com.sun.jna.Memory;
import com.sun.jna.ptr.ByteByReference;

public class SMSService {

    public static int SMSRstartService(int port) {
        if(SMS.INSTANCE.SMSSeviceStarted()) {
            SMSStopSerice();
        }
        return SMSStartService(port);
    }

    public static int SMSStartService(int port) {
        return SMS.INSTANCE.SMSStartService(port, 115200, 2, 8, 0, 0, "card");
    }

    public static int SMSStopSerice() {
        return SMS.INSTANCE.SMSStopSerice();
    }

    public static int SMSSendMessage(String Msg,String PhoneNo) {
        return SMS.INSTANCE.SMSSendMessage(Msg, PhoneNo);
    }

    public static int SMSReport(int index) {
        int r = SMS.INSTANCE.SMSQuery(index);
        if(r > 0) {
            SMSReportStruct.ByReference report = new SMSReportStruct.ByReference();
            report.index = index;
            SMS.INSTANCE.SMSReport(report);
            return report.Success;
        }
        return 0;
    }

    public static String SMSGetLastError() {
        ByteByReference error = new ByteByReference();
        Memory mymem = new Memory(100);
        error.setPointer(mymem);
        SMS.INSTANCE.SMSGetLastError(error, 100);
        byte[] str = new byte[100];
        mymem.read(0, str, 0, 100);
        return (new String(str)).trim();
    }
}
