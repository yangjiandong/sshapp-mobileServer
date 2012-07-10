package org.ssh.pm.mob.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.utils.MobilePhoneNumberUtil;
import org.ssh.pm.sms.ContentRule;
import org.ssh.pm.sms.SMSConfigReader;
import org.ssh.pm.sms.SMSService;
import org.ssh.pm.sms.SMSThread;
import org.ssh.pm.sms.entity.CheckLog;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.entity.User;

@Service
@Transactional
public class SendSMSService {
    private static Logger logger = LoggerFactory.getLogger(SendSMSService.class);

    @Autowired
    private UserDao userDao;

    HibernateDao<CheckLog, Long> checkLogDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        checkLogDao = new HibernateDao<CheckLog, Long>(sessionFactory, CheckLog.class);
    }

    private static boolean open;

    private ExecutorService single = Executors.newSingleThreadExecutor();

    static {
        int port = Integer.parseInt(SMSConfigReader.getReader().getSMSConfig("SMSPort"));
        int start = SMSService.SMSRstartService(port);
        if (start != 0)
            open = true;
        else {
            open = false;
            logger.error("短信猫打开异常");
            System.out.println();
        }
    }

    public void sendSMSToUsers(String content, String[] uids) {
        for (String receiver : uids) {
            User user = userDao.findUniqueBy("id", new Long(receiver.trim()));
            sendSMSToUser(content, user);
        }
    }

    public void sendSMSToUsers(String content, User[] receivers) {
        for (User receiver : receivers) {
            sendSMSToUser(content, receiver);
        }
    }

    public boolean sendSMSToUser(String content, User receiver) {
        if (!open)
            return false;
        if (ContentRule.isValid(content) && MobilePhoneNumberUtil.canBeSent(receiver.getMobileNo())) {
            SMSThread send = new SMSThread(content, receiver.getMobileNo());
            single.execute(send);
            return true;
        }
        return false;
    }

    public int sendExamineMessage(CheckLog log) {
        List<User> users = getUsers(log.getChecker());
        boolean sent = false;
        for (User user : users) {
            sent = sendSMSToUser(ContentRule.getExamineMessageContent(log, user.getName()), user);
        }
        if (sent) {
            return 1;
        } else
            return -1;
    }

    private List<User> getUsers(String name) {
        List<User> users = new ArrayList<User>();
        String temp;
        if (name == null) {
            temp = "";
        } else
            temp = name;
        while (true) {
            int f = temp.indexOf(",");
            if (f < 0) {
                User user = userDao.findUniqueBy("name", temp);
                if (user != null)
                    users.add(user);
                break;
            }
            String u = temp.substring(0, f);
            User user = userDao.findUniqueBy("name", u);
            if (user != null)
                users.add(user);
            temp = temp.substring(f + 1, temp.length());
        }
        return users;
    }

    //后台任务发送
    public void execute() {
        List<CheckLog> unsent = checkLogDao.find("from CheckLog where smssent = 0");
        for (CheckLog msg : unsent) {
            int res = sendExamineMessage(msg);
            msg.setSmssent(res);
            checkLogDao.save(msg);
        }
    }
}