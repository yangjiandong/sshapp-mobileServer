package org.ssh.pm.common.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.GetNo;

@Repository("getNoDao")
public class GetNoDao extends HibernateDao<GetNo, String> {

    //取得指定编号类型的值
    public Long getNextNoValue(String noName) {
        Long n = 1L;
        GetNo getNo = this.findUniqueBy("noName", noName);
        if (getNo == null) {
            getNo = new GetNo();
            getNo.setNoName(noName);
            getNo.setNoValue(n + 1L);
            save(getNo);
        } else {
            n = getNo.getNoValue();

            //+1
            getNo.setNoValue(n + 1L);
            save(getNo);
        }

        return n;
    }

    //补0
    public String getNextNoValueString(int len, String noName) {
        Long n = getNextNoValue(noName);
        String ns = n.toString();
        StringBuffer fillZero = new StringBuffer();
        for (int i = 0; i < len - ns.length(); i++) {
            fillZero.append("0");
        }
        fillZero.append(ns);
        return fillZero.toString();
    }
}
