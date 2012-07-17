package org.ssh.pm.mob;

public class MobConstants {

    public static final String MOB_QUERY = "01";//全院概况

    public static final String MOB_VITALSIGN_ONE = "1";//生命体征测量:一天一次
    public static final String MOB_VITALSIGN_MORE = "2";//生命体征测量:一天多次

    //存储过程
    public static final String MOB_SPNAME_GET_PATIENT = "sp_mob_get_patient";
    public static final String MOB_SPNAME_GET_VITALSIGN = "sp_mob_get_vitalsign";
    public static final String MOB_SPNAME_COMMIT_VITALSIGN = "sp_mob_commit_vitalsign";

    public static final String MOB_VITALSIGN_STATE_UPDATE = "Y";
    public static final String MOB_VITALSIGN_STATE_QUERY = "N";

}
