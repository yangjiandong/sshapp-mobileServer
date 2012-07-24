package org.ssh.pm.mob;

public class MobConstants {

    public static final String MOB_QUERY = "01";//全院概况

    //存储过程
    public static final String MOB_SPNAME_GET_PATIENT = "sp_mob_get_patient";
    public static final String MOB_SPNAME_GET_PATIENT_ALL = "sp_mob_get_patient_all";
    public static final String MOB_SPNAME_GET_VITALSIGN = "sp_mob_get_vitalsign";
    public static final String MOB_SPNAME_COMMIT_VITALSIGN = "sp_mob_commit_vitalsign";

    public static final String MOB_SPNAME_GET_DRUGCHECK = "sp_mob_drug_check";
    public static final String MOB_SPNAME_COMMIT_DRUGCHECK = "sp_mob_commit_drug_check";

    public static final String MOB_VITALSIGN_STATE_UPDATE = "Y";
    public static final String MOB_VITALSIGN_STATE_QUERY = "N";

    public static final String MOB_WORKLOAD_TYPE_DRUGCHECK = "1";

}
