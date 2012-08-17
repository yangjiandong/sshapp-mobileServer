package org.ssh.pm.approval.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.approval.entity.DrugApprovalData;

@Repository("drugApprovalDao")
public class DrugApprovalDao extends HibernateDao<DrugApprovalData, Long> {

}
