package org.ssh.pm.hcost.web;

import java.io.Serializable;

import org.ssh.pm.hcost.web.state.AnalyseState;
import org.ssh.pm.hcost.web.state.GatherState;
import org.ssh.pm.hcost.web.state.ImportState;
import org.ssh.pm.hcost.web.state.SumupState;
import org.ssh.sys.entity.PartDB;
import org.ssh.sys.entity.Resource;
import org.ssh.sys.entity.User;

//用户session信息
public class UserSession implements Serializable {
    private static final long serialVersionUID = -5891346223278285869L;

    private User account;
    String clientIp;
    String currentPassword;//当前密码
    String loginTime;

    PartDB partDb;//所选分院
    private Long moduleId;//所选分系统
    private boolean hasResource;
    String moduleName;//
    Resource resource;
    ImportState importState;//导入
    GatherState gatherState;//采集
    SumupState sumupState;
    AnalyseState analyseState;
    //从appsetup中取出user.edit.by.default的值,供基础数据维护时判断是否更新分院数据
    private String usereditbydefault;

    QueryParam queryParam;

    AnalyseParam analyseParam;

    public static class QueryParam {
        Long orgId;//所选核算单元
        String orgCode;
        String orgName;
        String itemCode;//所选损益表项目
        String startDate;//所选开始日期
        String endDate;//所选结束日期
        String checkOutCode;//所选病人费别
        Long orderNo;//查询明细当前顺序号
        String detailCode;//归集项目编号
        String hasInsert;//标记是否已经插入临时表,填入当前页面序号
        String reportNo;//报表编号
        String typeCode;

        String[][] params;

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getCheckOutCode() {
            return checkOutCode;
        }

        public void setCheckOutCode(String checkOutCode) {
            this.checkOutCode = checkOutCode;
        }

        public Long getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Long orderNo) {
            this.orderNo = orderNo;
        }

        public String getDetailCode() {
            return detailCode;
        }

        public void setDetailCode(String detailCode) {
            this.detailCode = detailCode;
        }

        public String getHasInsert() {
            return hasInsert;
        }

        public void setHasInsert(String hasInsert) {
            this.hasInsert = hasInsert;
        }

        public String[][] getParams() {
            return params;
        }

        public void setParams(String[][] params) {
            this.params = params;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getReportNo() {
            return reportNo;
        }

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

    }

    public static class AnalyseParam {
        Long orgId;//所选核算单元
        String orgCode;
        String orgName;
        String currentStartDate;//当前开始日期
        String currentEndDate;//当前结束日期
        String baseStartDate;//基期开始日期
        String baseEndDate;//基期结束日期
        String checkOutCode;//所选病人费别

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getCurrentStartDate() {
            return currentStartDate;
        }

        public void setCurrentStartDate(String currentStartDate) {
            this.currentStartDate = currentStartDate;
        }

        public String getCurrentEndDate() {
            return currentEndDate;
        }

        public void setCurrentEndDate(String currentEndDate) {
            this.currentEndDate = currentEndDate;
        }

        public String getBaseStartDate() {
            return baseStartDate;
        }

        public void setBaseStartDate(String baseStartDate) {
            this.baseStartDate = baseStartDate;
        }

        public String getBaseEndDate() {
            return baseEndDate;
        }

        public void setBaseEndDate(String baseEndDate) {
            this.baseEndDate = baseEndDate;
        }

        public String getCheckOutCode() {
            return checkOutCode;
        }

        public void setCheckOutCode(String checkOutCode) {
            this.checkOutCode = checkOutCode;
        }

    }

    public UserSession(User account) {
        this.account = account;
    }

    public User getAccount() {
        return account;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public PartDB getPartDb() {
        return partDb;
    }

    public void setPartDb(PartDB partDb) {
        this.partDb = partDb;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public ImportState getImportState() {
        return importState;
    }

    public void setImportState(ImportState importState) {
        this.importState = importState;
    }

    public GatherState getGatherState() {
        return gatherState;
    }

    public void setGatherState(GatherState gatherState) {
        this.gatherState = gatherState;
    }

    public SumupState getSumupState() {
        return sumupState;
    }

    public void setSumupState(SumupState sumupState) {
        this.sumupState = sumupState;
    }

    public String getUsereditbydefault() {
        return usereditbydefault;
    }

    public void setUsereditbydefault(String usereditbydefault) {
        this.usereditbydefault = usereditbydefault;
    }

    public QueryParam getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(QueryParam queryParam) {
        this.queryParam = queryParam;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public AnalyseState getAnalyseState() {
        return analyseState;
    }

    public void setAnalyseState(AnalyseState analyseState) {
        this.analyseState = analyseState;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public boolean isHasResource() {
        return hasResource;
    }

    public void setHasResource(boolean hasResource) {
        this.hasResource = hasResource;
    }

    public AnalyseParam getAnalyseParam() {
        return analyseParam;
    }

    public void setAnalyseParam(AnalyseParam analyseParam) {
        this.analyseParam = analyseParam;
    }

}
