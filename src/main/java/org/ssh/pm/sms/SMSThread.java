package org.ssh.pm.sms;

public class SMSThread extends Thread {
    private String content;
    private String phonenumber;

    public SMSThread(String content, String phonenumber) {
        this.content = content;
        this.phonenumber = phonenumber;
    }

    public void run() {
        System.out.println("发送消息 + to " + phonenumber + "内容 " + content);
        int index = SMSService.SMSSendMessage(content, phonenumber);
    }
}
