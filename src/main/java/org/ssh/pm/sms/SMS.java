package org.ssh.pm.sms;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;

public interface SMS extends Library {
    SMS INSTANCE = (SMS) Native.loadLibrary("SMSDLL.dll", SMS.class);

    public static class SMSReportStruct extends Structure {
        public static class ByReference extends SMSReportStruct implements
                Structure.ByReference {
        }

        public static class ByValue extends SMSReportStruct implements
                Structure.ByValue {
        }

        public int index;
        public byte[] Msg = new byte[255];
        public int Success;
        public byte[] PhoneNo = new byte[31];
    }

    public int SMSStartService(int nPort, int BaudRate, int Parity,
            int DataBits, int StopBits, int FlowControl, String csca);

    public int SMSStopSerice();

    public int SMSSendMessage(String Msg, String PhoneNo);

    public int SMSQuery(int index);

    public int SMSReport(Structure.ByReference rept);

    public int SMSGetLastError(ByteByReference err, int nlen);

    public boolean SMSSeviceStarted();
}
