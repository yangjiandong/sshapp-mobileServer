package org.ssh.pm.enums;

public class QueryConstants {

    public static final String COST = "1";
    public static final String INCOME = "2";
    public static final String IN = "IN";
    public static final String NOTIN = "NOT IN";
    public static final String LIKE = "LIKE";
    public static final String NOTLIKE = "NOT LIKE";

    public static final String INCOME_OUT = "01";//门诊收入
    public static final String INCOME_IN = "02";//住院收入
    public static final String INCOME_ALL = "05";//总收入

    public static final String COST_DIRECT = "01";//直接成本
    public static final String COST_INDIRECT = "02";//间接成本
    public static final String COST_REGULAR = "03";//固定成本
    public static final String COST_VARIABLE = "04";//变动成本
    public static final String COST_ALL = "05";//总成本

    //查询用的条件
    //总成本
    public static final String QUERY_COSTTOTAL = " and a.DetailCode in "
            + "(select detailcode from t_sumupdetail where type1 = 1)";
    //直接成本
    public static final String QUERY_DIRECTCOST = " and a.DetailCode in "
            + "(select detailcode from t_sumupdetail where type1 = 1 and type2 in (1,2))";
    //医疗技术成本
    public static final String QUERY_MEDICAL = " and a.DetailCode in ('A3030','A4020','A4030','A4040')";
    //辅助成本
    public static final String QUERY_ASSISTANTCOST = " and a.DetailCode in ('A3020')";
    //管理成本
    public static final String QUERY_MANAGEMENTCOST = " and a.DetailCode in ('A3010')";
    //药品购入成本
    public static final String QUERY_DRUGBUYCOST = " and a.DetailCode in ('A1203','A1201')";
    //材料成本
    public static final String QUERY_ITEMCOST = " and a.DetailCode in ('A1301','A1303','A1302','A1304','A1401')";

    //分摊成本
    public static final String QUERY_APPORTIONCOST = " and a.DetailCode in ('A2001')";
    //总收入  科级
    public static final String QUERY_INCOMETOTAL = " and a.detailCode in "
            + " (select detailcode from t_incomereportsource where itemcode in ( "
            + "select itemcode from t_reportitem where reportno =2 "
            + "and itemcode like '60103%'  and itemname in ('本科','转出','执行')))";

    //总收入  院级
    public static final String QUERY_INCOMETOTAL_YJ = " and a.detailCode in "
            + " (select detailcode from t_incomereportsource where itemcode in ( "
            + "select itemcode from t_reportitem " + " where itemcode like '501%'))";

    //门诊收入
    public static final String QUERY_OUTINCOME = " AND a.DetailCode in "
            + " (select detailcode from T_IncomeReportSource where itemcode in('60101010101','60101010103',"
            + "'60101010105','60101020101','60101020103','60101020105'))";
    //住院收入
    public static final String QUERY_ININCOME = " AND a.DetailCode in "
            + " (select detailcode from T_IncomeReportSource where itemcode in('60102010101','60102010103',"
            + "'60102010105','60102020101','60102020103','60102020105'))";
    //医技收入
    public static final String QUERY_MTINCOME = " and a.detailCode in "
            + " (select detailcode from t_incomereportsource where itemcode in ( "
            + "select itemcode from t_reportitem where reportno =2 "
            + "and itemcode like '60103%'  and itemname in ('执行')))";
    //药品收入
    public static final String QUERY_DRUGINCOME = " and a.DetailCode in ('A0101','A0301')";

    //直接成本,不包括药品和收费材料
    public static final String QUERY_DIRECTCOST2 = " and a.DetailCode in "
            + "(select detailcode from t_sumupdetail where type1 = 1 and type2 in (1,2) "
            + " and DetailCode not in ('A1203','A1201')) "
            + " and a.costcatcode not in (select costcatcode from TA_ExpendableCostCatCode ) ";

    //科级,毛收入
    public static final String HCOST_INCOME = " and a.detailCode in "
            + " (select detailcode from t_incomereportsource where itemcode in ( "
            + "select itemcode from t_reportitem where reportno =2 "
            + "and itemcode like '60103%'  and itemname in ('本科','转出','执行')))";

    //科级,临床收入
    public static final String HCOST_MECIAL_INCOME = " and a.detailCode in "
            + " (select detailcode from t_incomereportsource where itemcode in ( "
            + "select itemcode from t_reportitem where reportno =2 "
            + "and itemcode like '60103%'  and itemname in ('本科','转出')))";

    //科级,医技收入
    public static final String HCOST_METCH_INCOME = " and a.detailCode in "
            + " (select detailcode from t_incomereportsource where itemcode in ( "
            + "select itemcode from t_reportitem where reportno =2 "
            + "and itemcode like '60103%'  and itemname in ('执行')))";

    //科级,核算收入
    public static final String HCOST_RECKONINCOME = " and a.detailCode in "
            + " (select detailcode from t_incomereportsource where itemcode in ( "
            + "select itemcode from t_reportitem where reportno =2 "
            + "and itemcode like '60103%'  and itemname in ('合计')))";

    //科级,本科医疗组收入
    public static final String HCOST_INCOME_MEDICAL = " and a.detailCode in "
            + " (select detailcode from t_incomereportsource where itemcode in ( "
            + "select itemcode from t_reportitem where reportno =2 "
            + "and itemcode like '60103%'  and itemname in ('本科')))";

}
