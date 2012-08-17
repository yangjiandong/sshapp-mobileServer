package org.ssh.pm.approval.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.approval.dao.DrugApprovalDao;
import org.ssh.pm.enums.ApprovalConstants;
import org.ssh.pm.mob.MobConstants;
import org.ssh.pm.mob.service.SendSMSService;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.entity.User;

@Service
@Transactional
public class ApprovalService {
    private static Logger logger = LoggerFactory.getLogger(ApprovalService.class);

    @Autowired
    private DrugApprovalDao drugApprovalDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SendSMSService sendSMSService;

    public void sendMessage(String typeCode) {
        if (typeCode.equals(MobConstants.MOB_DRUGAPPR)) {
            op_Drug();
        }
        if (typeCode.equals(MobConstants.MOB_OPERAPPR)) {
            op_Oper();
        }
        if (typeCode.equals(MobConstants.MOB_DANGER)) {
            op_Danger();
        }
    }

    private void op_Drug() {

        List<User> list = userDao.find("select a from User a,DrugAppr+ovalData b where "
                + " (a.userNo = b.receiveWhoCode or a.departNo = b.receiveDeptCode) and b.state = ? ",
                ApprovalConstants.APPROVAL_GET);
        for (User u : list) {
            String content = "ekingsoft(" + MobConstants.MOB_DRUGAPPR + ")";
            content = content + ":您有一个用药申请的审批";
            sendSMSService.sendSMSToUser(content, u);
        }

    }

    private void op_Oper() {

    }

    private void op_Danger() {

    }
}