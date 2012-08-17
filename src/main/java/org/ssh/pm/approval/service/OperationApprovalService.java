package org.ssh.pm.approval.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.approval.dao.OperationApprovalDao;
import org.ssh.pm.approval.entity.OperationApprovalData;
import org.ssh.pm.enums.ApprovalConstants;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.entity.User;

@Service
@Transactional
public class OperationApprovalService {
    private static Logger logger = LoggerFactory.getLogger(OperationApprovalService.class);

    @Autowired
    private OperationApprovalDao operationApprovalDao;
    @Autowired
    private UserDao userDao;

    public void saveOperationApproval(String userId, String id, String result, String note) throws Exception {

        try {

            User user = userDao.findUniqueBy("id", Long.valueOf(userId));

            List<OperationApprovalData> list = operationApprovalDao.find(" from OperationApprovalData where id = ? ",
                    Long.valueOf(id));
            if (list.size() > 0) {
                OperationApprovalData entity = list.get(0);
                entity.setResult(result);
                entity.setNote(note);
                entity.setDealWho(user.getUserName());
                entity.setDealWhoCode(user.getUserNo());
                entity.setDealDate(operationApprovalDao.getNowString("yyyy-MM-dd HH:mm:ss"));
                entity.setState(ApprovalConstants.APPROVAL_SAVE);
                operationApprovalDao.save(entity);
            }

        } catch (Exception e) {
            logger.error("saveDrugApproval:", e.getMessage());
            throw new Exception(e);
        }

    }

    public List<OperationApprovalData> getAll(String userId) throws Exception {

        User user = userDao.findUniqueBy("id", Long.valueOf(userId));

        List<OperationApprovalData> list = operationApprovalDao.find(
                " from OperationApprovalData where state = ? and receiveWhoCode = ? ", ApprovalConstants.APPROVAL_GET,
                user.getUserNo());

        return list;

    }

    public OperationApprovalData getOne(String id) throws Exception {
        OperationApprovalData data = null;
        List<OperationApprovalData> list = operationApprovalDao.find(
                " from OperationApprovalData where id = ? and state = ? ", Long.valueOf(id),
                ApprovalConstants.APPROVAL_GET);
        if (list.size() > 0) {
            data = list.get(0);
        }
        return data;

    }

}