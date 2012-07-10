package org.ssh.pm.common.extjs;

/**
 * 网格列元数据
 */

public class GridColumnMeta {

    private String name;
    private String header;
    private boolean searchable;
    private String searchName; //dbobj中的字段名可能与name不同
    private String type;

    //string
    //int
    //float
    //bool
    //date //type: 'date', dateFormat: 'Y.m.d'

    public void setType(String obj) {
        this.type = obj;
    }

    public String getType() {
        return type;
    }

    public void setSearchName(String obj) {
        this.searchName = obj;
        if (obj == null || obj.length() == 0)
            this.searchName = this.name;
    }

    public String getSearchName() {
        return searchName;
    }

    public GridColumnMeta() {
        this.searchable = true;
    }

    public void setSearchable(boolean obj) {
        this.searchable = obj;
    }

    public boolean getSearchable() {
        return searchable;
    }

    public void setHeader(String obj) {
        this.header = obj;
    }

    public String getHeader() {
        return header;
    }

    public void setName(String name) {
        this.name = name;
        if (this.searchName == null)
            this.searchName = name;
    }

    public String getName() {
        return name;
    }

} //EOP
