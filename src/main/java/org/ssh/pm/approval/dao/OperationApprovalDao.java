package org.ssh.pm.approval.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.approval.entity.OperationApprovalData;

@Repository("operationApprovalDao")
public class OperationApprovalDao extends HibernateDao<OperationApprovalData, Long> {

}
