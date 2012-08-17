package org.ssh.pm.enums;

public enum SetupCode {
    AUTO_RUNJOB("user_AutoRunJob"), AUTO_RUNBEF_DAYS("user_AutoRun.Bef.Days"),
    //药品成本来源
    //SETUPCODE_DRUGCOST_FROM("user_DrugCostFrom"),
    //门诊或病区护理单元得到药品收入比例(%)
    SETUPCODE_INDRUGRATEOFNURSE("user_InDrugRateOfNurse"),

    ORG_CODE_PATTERN("user_org.code.pattern"),

    //医院信息
    HOSPITAL_INFO("user_Hospital.Info"),

    //DrugBuyCostCatCode,C,按药品定额计算药品成本时对应的成本分类
    DRUGBUYCOSTCATCODE("user_DrugBuyCostCatCode"),

    //判断是否在损益表2中显示按床日转移成本
    TRANSFER_BY_DAYSONBED("user_transfer.by.daysonbed"),

    //科室核算转移成本,是否转出全部(直接间接)
    TRANSFER_ALL_COST("user_transfer.allcost"),

    //
    DEFAULT_PAGE("user_default.page"),

    //
    USE_DRUGFACTOR("user_useDrugFactor"),

    //报表打印每页行数
    PRINT_ROWS("user_Print_rows");

    private final String value;

    private SetupCode(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
