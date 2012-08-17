package org.ssh.pm.sms;

import org.ssh.pm.sms.entity.CheckLog;


public class ContentRule {
    // private static final String formate =
    // SMSConfigReader.getReader().getSMSConfig("SMSFormate");
    // private static final String ExamineFormate =
    // ConfigReader.getReader().getConfigValue(
    // ConfigReader.SMSConfig, "CheckFormate");

    public static final String LENGTHPATTEN = "%length%";
    public static final String CONTENTATTEN = "%content%";
    public static final String PARAMETER = "%parameter%";
    private static final String VARLEN = "%varLen%";
    private static String CONTENTRULE = SMSConfigReader.getReader()
            .getSMSConfig("CheckFormate");

    public static String getContent(String content, String formate, CheckLog log) {
        String result = SMSConfigReader.getReader().getSMSConfig(formate);
        String text = ASCIIUtil.encode(content);
        String para = "link=redirect.mas?Type=ARForm|" + log.getId();
        result = result.replaceAll(PARAMETER, para);
        result = result.replaceAll(VARLEN, para.length() + "");
        result = result.replaceAll(LENGTHPATTEN, text.length() + "");
        result = result.replaceAll(CONTENTATTEN, text);
        return result;
    }

    public static boolean isValid(String content) {
        if (content == null || content.isEmpty() || content.trim().equals(""))
            return false;
        return true;
    }

    public static String getExamineMessageContent(CheckLog log, String name) {
        String content = CONTENTRULE;
        content = content.replaceAll("%FDate%", log.getDate() + "");
        content = content.replaceAll("%FCheckDate%", log.getCheckdate() + "");
        content = content.replaceAll("%FCheckNote%", log.getChecknote() + "");
        content = content.replaceAll("%FName%", log.getName());
        content = content.replaceAll("%FDoctor%", log.getDoctor());
        content = content.replaceAll("%FCheckDept%", log.getCheckDept());
        content = content.replaceAll("%FChecker%", name);
        content = content.replaceAll("%FAuxInfo1%", log.getAuxinfo1());
        content = content.replaceAll("%FAuxInfo2%", log.getAuxinfo2());
        content = content.replaceAll("%FAuxInfo3%", log.getAuxinfo3());
        content = content.replaceAll("%FAuxInfo4%", log.getAuxinfo4());
        content = content.replaceAll("%FLabe1%", log.getLabel1());
        content = content.replaceAll("%FLabe2%", log.getLabel2());
        content = content.replaceAll("%FLabe3%", log.getLabel3());
        content = content.replaceAll("%FLabe4%", log.getLabel4());
        content = content.replaceAll("%FLabe5%", log.getLabel5());
        content = content.replaceAll("%FLabe6%", log.getLabel6());
        content = content.replaceAll("%FLabe7%", log.getLabel7());
        content = content.replaceAll("%FLabe8%", log.getLabel8());
        content = content.replaceAll("%FLabe9%", log.getLabel9());
        content = content.replaceAll("%FLabe10%", log.getLabel10());
        content = content.replaceAll("%FLabe11%", log.getLabel11());
        content = content.replaceAll("%FLabe12%", log.getLabel12());
        content = content.replaceAll("%FLabe13%", log.getLabel13());
        content = content.replaceAll("%FLabe14%", log.getLabel14());
        content = content.replaceAll("%FLabe15%", log.getLabel15());
        content = content.replaceAll("%FField1%", log.getField1());
        content = content.replaceAll("%FField2%", log.getField2());
        content = content.replaceAll("%FField3%", log.getField3());
        content = content.replaceAll("%FField4%", log.getField4());
        content = content.replaceAll("%FField5%", log.getField5());
        content = content.replaceAll("%FField6%", log.getField6());
        content = content.replaceAll("%FField7%", log.getField7());
        content = content.replaceAll("%FField8%", log.getField8());
        content = content.replaceAll("%FField9%", log.getField9());
        content = content.replaceAll("%FField10%", log.getField10());
        content = content.replaceAll("%FField11%", log.getField11());
        content = content.replaceAll("%FField12%", log.getField12());
        content = content.replaceAll("%FField13%", log.getField13());
        content = content.replaceAll("%FField14%", log.getField14());
        content = content.replaceAll("%FField15%", log.getField15());
        content = content.replaceAll("%FTypeName%", log.getTypename());
        if (log.getType() == 1) {
            return getContent(content, "SMSShoushu", log);
        } else if (log.getType() == 2) {
            return getContent(content, "SMSheliyongyao", log);
        } else
            return getContent(content, "SMSFormate", log);
    }
}
