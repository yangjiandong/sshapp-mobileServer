package org.ssh.pm.enums;

public class CoreConstants {

    // barcode
    // public static final int BARCODE_LEN = 12;
    // public static final int BARCODE_PREFIX_LEN = 2;

    //列表数据分割符
    public static final String DATA_SEPARATOR = ",";

    public static final String ADMIN_NAME = "Admin";
    public static final String INIT_PASSWORD = "12345";
    public static final String USER_ENABLED = "enabled";
    public static final String USER_DISABLED = "disabled";

    // 表示业务实体的当前状态信息{'Y':启用,'N':停用}
    public static final String ACTIVE = "Y";
    public static final String INACTIVE = "N";

    // session中存放当前登录用户Id的Key
    public static final String SESSION_KEY_USER = "userId";
    public static final String SESSION_NAME = "userSession";

    // 固定码
    public static final String REGULARCODE = "gd";

    //报表项目类型:区分收入类和成本类
    public static final String ITEMTYPE_COST = "1";
    public static final String ITEMTYPE_INCOME = "2";

    //报表项目类型:区分成本(经营)核算和财务核算
    public static final String RECKONTYPE_MANAGEMENT = "1";
    public static final String RECKONTYPE_FISCAL = "2";

    //系统定义报表项目编码(用于计算损益)
    public static final String ITEMCODE_RECKONED_INCOME_TOTAL = "3";
    public static final String ITEMCODE_COST_TOTAL = "1";
    public static final String ITEMCODE_INCOME_TOTAL = "2";
    public static final String ITEMCODE_OTHER_INCOME_TOTAL = "4";

    //系统定义报表项目编码(用于收入查询)
    public static final String ITEMCODE_INCOME_OUTPAT = "201"; //门诊收入
    public static final String ITEMCODE_INCOME_INPAT = "202"; //住院收入
    public static final String ITEMCODE_INCOME_MEDICALTECH = "203"; //医技收入
    public static final String ITEMCODE_INCOME_RECKONED_OUTPAT = "301"; //门诊核算收入
    public static final String ITEMCODE_INCOME_RECKONED_INPAT = "302"; //住院核算收入
    public static final String ITEMCODE_INCOME_RECKONED_MEDICALTECH = "303"; //医技核算收入

    public static final String ITEMCODE_INCOME_HIS_INPAT = "204"; //HIS住院收入
    public static final String ITEMCODE_INCOME_HIS_INPAT_DRUG = "20401"; //HIS住院药品收入
    public static final String ITEMCODE_INCOME_HIS_INPAT_MEDICAL = "20402"; //HIS住院检疗收入

    public static final String ITEMCODE_INCOME_HIS_OUTPAT = "205"; //HIS门诊收入
    public static final String ITEMCODE_INCOME_HIS_OUTPAT_DRUG = "20501"; //HIS门诊药品收入
    public static final String ITEMCODE_INCOME_HIS_OUTPAT_MEDICAL = "20502"; //HIS门诊检疗收入

    //要求生成按月数据的报表项目
    public static final String ITEMCODE_DRUG_BUY_COST = "10201"; //药品购入成本
    public static final String ITEMCODE_DRUG_BUY_COST2 = "10298"; //药品购入成本2
    public static final String ITEMCODE_DRUG_BUY_COST3 = "10299"; //药品购入成本3
    public static final String ITEMCODE_DRUG_BUY_COST4 = "10297"; //药品购入成本4

    public static final String DATA_STATE_ACCOUNTED = "1";//已上账
    public static final String DATA_STATE_NOACCOUNT = "0";//未上账

    public static final String INPUTTYPE_COST = "1";
    public static final String INPUTTYPE_INCOME = "2";

    public static final String ALL = "1"; //两者
    public static final String MANAGEMENT = "2"; //仅用于管理核算
    public static final String FISCAL = "3"; //仅用于会计核算

    //大类
    public static final String TYPE_COST = "1";
    public static final String TYPE_INCOME = "2";

    //小类（成本）
    public static final String TYPE_DIRECT_COST = "1";
    public static final String TYPE_APPORTION_COST = "2";
    public static final String TYPE_TRANSFER_COST = "3";

    //小类（收入）
    public static final String TYPE_INCOME_OUT = "1";
    public static final String TYPE_INCOME_IN = "2";
    public static final String TYPE_INCOME_MEDICALTECH = "3";
    public static final String TYPE_INCOME_OTHER = "4";

    //系统定义 DetailCode
    public static final String DETAILCODE_DRUGBUYCOST = "3001"; //药品购入成本
    public static final String DETAILCODE_DRUGBUYCOST2 = "3001A"; //分类药品购入成本
    public static final String DETAILCODE_INPUTCOST_BYCHECKOUT = "0511"; //手工录入的成本(已区分病人费别)
    public static final String DETAILCODE_INPUTINCOME_BYCHECKOUT = "0512"; //手工录入的收入(已区分病人费别)

    //不需要进行按病人费别划分成本的归集项
    public static final String DETAILCODE_COST_PARTITION_EXCLUDED = "'" + DETAILCODE_DRUGBUYCOST + "','"
            + DETAILCODE_DRUGBUYCOST2 + "','" + DETAILCODE_INPUTCOST_BYCHECKOUT + "'";

    //操作类型
    public static final String TYPE_IMPORT = "00"; //导入
    public static final String TYPE_GATHER = "01"; //采集
    public static final String TYPE_SUMUP = "02"; //归集

    public static final String TYPE_ANALYSE = "03";//分析

    //导入操作状态
    public static final Integer STATE_NOTHING = 0;
    public static final Integer STATE_SUCCEED = 1;//成功-在操作成功的基础上，提示信息也全部读取完成
    public static final Integer STATE_FAIL = 2;//失败
    public static final Integer STATE_DOING = 3;//正在进行
    public static final Integer STATE_VALID_FAIL = 4;//验证失败
    public static final Integer STATE_BREAK = 9;//终止
    public static final Integer STATE_RUNSUCCEED = 5;//操作成功

    public static final Integer READED = 1;//已读
    public static final Integer NOREAD = 0;//未读

    //自动导入、手工导入
    public static final String RUN_AUTO = "run_by_auto";
    public static final String RUN_MAN = "run_by_man";
    public static final String AUTORUN_USERNAME = "自动任务";
    public static final int AUTORUN_USERID = 0;

    //继续操作还是停止
    public static final Integer ACTION_DOING = 1;
    public static final Integer ACTION_STOP = 0;

    public static final String REPORT_TYPE_PANDL = "1";//损益表
    public static final String REPORT_TYPE_PANDL_SUM = "2";//损益汇总表

    //转移类别
    public static final String ADMINISTRATION = "1"; //行政管理
    public static final String LOGISTICS = "2"; //后勤
    public static final String MEDICAL = "3"; //医疗(床日成本)

    public static final String ADMINISTRATION_NAME = "行政管理部门成本转移方案";
    public static final String LOGISTICS_NAME = "后勤部门成本转移方案";
    public static final String MEDICAL_NAME = "床日成本转移方案";

    //系统参数
    //判断是否仅由全院维护基础字典
    public static final String EDIT_BY_DEFAULT = "user_edit.by.default";
    //判断是否是军队医院
    public static final String HOSPITAL_IS_ARMY = "user_hospital.is.army";

    //转移基础
    public static final String EMPLOYEE_COUNT = "01"; //按职工人数
    public static final String AREA = "02";//面积
    public static final String DAYS_ONBED = "03"; //按实际床日数
    public static final String DAYS_OUT = "04"; //按出院人数
    public static final String BED_USE = "05"; //按开放床位
    public static final String OUT_TIME = "06"; //按门诊人次
    public static final String INCOME = "07"; //按业务收入
    public static final String RECKONINCOME = "08"; //按核算收入
    public static final String OTHER = "99";//其他

    //按业务收入转移,科室类别
    public static final String OPS = "1"; //手术室
    public static final String MEDICAL_TECH = "2"; //医技科室
    public static final String DRUG_MANAGE = "3"; //药品管理部门

    public static final String OPS_NAME = "手术室";
    public static final String MEDICAL_TECH_NAME = "医技科室";
    public static final String DRUG_MANAGE_NAME = "药品管理部门";

    //checkout-all
    public static final String CHECKOUT_ALL = "全部记录";

    //
    public static final String USER_UPDATED_PASSWD = "1";//已修改
    public static final String USER_NOUPDATE_PASSWD = "0";//

    public static final String TYPECODE_HCOST = "01";//成本核算

    public static final String TYPEITEM_DRUG = "01";//药品
    public static final String TYPEITEM_FIX = "02";//固定资产
    public static final String TYPEITEM_EXP1 = "03";//器械
    public static final String TYPEITEM_EXP2 = "04";//后勤
    public static final String TYPEITEM_SUP = "05";//供应室

    //
    public static final String HEADLIST = "headNameList";

    //
    //  RED("FF0000"),
    //  ORANGE("FFA500"),
    //  YELLOW("FFFF00"),
    //  GREEN("008000"),
    //  BLUE("0000FF"),
    //  INDIGO("4B0082"),
    //  VIOLET("EE82EE");

    // 预定义的颜色
    // http://www.color-hex.com/
    public static final String[] MORECOLORS = { "FF0000", "FFA500", "FFFF00", "008000", "0000FF", "4B0082", "EE82EE",
            "67babb", "b5c5e9", "f2f85b", "bab36e", "366c4f", "8af71c", "a2d2a9", "c3321a", "1db6aa", "739c87",
            "aa8f65", "66ba8f", "f9e1cb", "eac9ff", "600510", "4dc044", "f91912", "1a01b7", "42326b", "0994da",
            "db4cb9", "4deb5e", "1e2392", "812a55", "e7f071", "108ad7", "b1cd0c" };

    public static final String COLOR_RED = "FF0000";//红色
    public static final String COLOR_ORANGE = "FFA500";//橙色
    public static final String COLOR_GREEN = "008000";//绿色
    public static final String COLOR_ORCHID = "800080";//紫色
    public static final String COLOR_BLUE = "0000FF";//蓝色

    public static final String CLINICALPATH_DRUG = "1";
    public static final String CLINICALPATH_ITEM = "2";

    public static final String REPORTDATA_COST_DIRECT = "1";
    public static final String REPORTDATA_COST_APPORTION = "2";
    public static final String REPORTDATA_COST_MANAGERMENT = "3";
    public static final String REPORTDATA_COST_DEPRECIATION = "4";
    public static final String REPORTDATA_COST_DRUG = "5";
    public static final String REPORTDATA_COST_MEDICAL = "6";
    public static final String REPORTDATA_COST_BED = "7";

}
