package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.sys.entity.AppSetup;

@Repository("appSetupDao")
public class AppSetupDao extends SysdataHibernateDao<AppSetup, Long> {

    public String getSetupValue(String setupCode) {

        String setupValue = "";

        AppSetup a = this.findUniqueBy("setupCode", setupCode);
        if (a != null) {
            setupValue = a.getSetupValue();
        }
        return setupValue;
    }

    public boolean checkArmy() {
        boolean flag = false;

        AppSetup app = findUniqueBy("setupCode", "user_hospital.is.army");
        if (app != null) {
            flag = app.getSetupValue().equalsIgnoreCase(CoreConstants.ACTIVE) ? true : false;
        }

        return flag;
    }
}
