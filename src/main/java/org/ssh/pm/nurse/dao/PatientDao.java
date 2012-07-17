package org.ssh.pm.nurse.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.nurse.entity.Patient;

@Repository("patientDao")
public class PatientDao extends HibernateDao<Patient, Long> {

}
