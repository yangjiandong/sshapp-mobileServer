package org.ssh.pm.approval.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.approval.entity.ApprovalNote;

@Repository("approvalNoteDao")
public class ApprovalNoteDao extends HibernateDao<ApprovalNote, Long> {

}
