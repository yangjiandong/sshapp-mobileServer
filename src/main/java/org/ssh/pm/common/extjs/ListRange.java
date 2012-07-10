package org.ssh.pm.common.extjs;

import java.io.Serializable;

/**
 * 网格取数通用返回值类
 */
public class ListRange implements Serializable {
    private static final long serialVersionUID = -2322678718091004704L;

    private Object[] data;
    private int totalSize;
    private boolean success;

    private String exportFileName;

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public String getExportFileName() {
        if (exportFileName == null) {
            exportFileName = "o.xls";
        }
        return exportFileName;
    }

} //EOP
