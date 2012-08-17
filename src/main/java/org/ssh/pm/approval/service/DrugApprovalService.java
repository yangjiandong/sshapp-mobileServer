package org.ssh.pm.approval.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.approval.dao.ApprovalNoteDao;
import org.ssh.pm.approval.dao.DrugApprovalDao;
import org.ssh.pm.approval.entity.ApprovalNote;
import org.ssh.pm.approval.entity.DrugApprovalData;
import org.ssh.pm.enums.ApprovalConstants;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.entity.User;

@Service
@Transactional
public class DrugApprovalService {
    private static Logger logger = LoggerFactory.getLogger(DrugApprovalService.class);

    @Autowired
    private DrugApprovalDao drugApprovalDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ApprovalNoteDao approvalNoteDao;

    public void saveDrugApproval(String userId, String appNo, String result, String note) throws Exception {

        try {

            User user = userDao.findUniqueBy("id", Long.valueOf(userId));

            List<DrugApprovalData> list = drugApprovalDao.find(" from DrugApprovalData where appNo = ? ",
                    Long.valueOf(appNo));
            if (list.size() > 0) {
                DrugApprovalData entity = list.get(0);
                entity.setResult(result);
                entity.setNote(note);
                entity.setDealWho(user.getUserName());
                entity.setDealWhoCode(user.getUserNo());
                entity.setDealDate(drugApprovalDao.getNowString("yyyy-MM-dd HH:mm:ss"));
                entity.setState(ApprovalConstants.APPROVAL_SAVE);
                drugApprovalDao.save(entity);
            }

        } catch (Exception e) {
            logger.error("saveDrugApproval:", e.getMessage());
            throw new Exception(e);
        }

    }

    public List<DrugApprovalData> getAll(String userId) throws Exception {

        User user = userDao.findUniqueBy("id", Long.valueOf(userId));

        List<DrugApprovalData> list = drugApprovalDao.find(
                " from DrugApprovalData where state = ? and (receiveWhoCode = ? or receiveDeptCode = ? )",
                ApprovalConstants.APPROVAL_GET, user.getUserNo(), user.getDepartNo());

        return list;

    }

    public DrugApprovalData getOne(String appNo) throws Exception {
        DrugApprovalData data = null;
        List<DrugApprovalData> list = drugApprovalDao.find(" from DrugApprovalData where appNo = ? and state = ? ",
                Long.valueOf(appNo), ApprovalConstants.APPROVAL_GET);
        if (list.size() > 0) {
            data = list.get(0);
        }
        return data;

    }

    @Transactional(readOnly = true)
    public List<ApprovalNote> queryNote() {

        List<ApprovalNote> list = approvalNoteDao.find(" from ApprovalNote order by id");
        return list;

    }

    @Transactional(readOnly = true)
    public String validNote(String id, String code, String name, String typeCode) {

        StringBuffer error = new StringBuffer();
        if (StringUtils.isNotBlank(id)) {

            List<ApprovalNote> list = approvalNoteDao
                    .find(" from ApprovalNote where id != ? and name = ? and typeCode = ? ", Long.valueOf(id), name,
                            typeCode);
            if (list != null && list.size() > 0)
                error.append("备注信息不能重复");

        } else {
            List<ApprovalNote> list = approvalNoteDao.find(" from ApprovalNote where name = ? and typeCode = ? ", name,
                    typeCode);
            if (list != null && list.size() > 0)
                error.append("备注信息不能重复");

        }

        return error.toString();
    }

    public void saveNote(String id, String code, String name, String typeCode) {

        try {
            if (StringUtils.isNotBlank(id)) {
                ApprovalNote item = approvalNoteDao.findUniqueBy("id", Long.valueOf(id));
                item.setName(name);
                item.setCode(code);
                item.setTypeCode(typeCode);
                approvalNoteDao.save(item);
            } else {
                ApprovalNote item = new ApprovalNote();
                item.setName(name);
                item.setCode(code);
                item.setTypeCode(typeCode);
                approvalNoteDao.save(item);
            }
        } catch (Exception e) {
            logger.error("saveItem:", e.getMessage());
            throw new ServiceException("备注信息定义失败");
        }

    }

    public void deleteNote(Long id) {
        try {

            approvalNoteDao.batchExecute("delete from ApprovalNote where id = ? ", id);

        } catch (Exception e) {
            logger.error("deleteNote:", e.getMessage());
            throw new ServiceException("删除失败");
        }
    }

    @Transactional(readOnly = true)
    public List<ApprovalNote> getNote(String typeCode) {

        List<ApprovalNote> list = approvalNoteDao.find(" from ApprovalNote where typeCode = ? ", typeCode);
        return list;

    }
}