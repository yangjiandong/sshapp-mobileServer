package org.ssh.pm.common.extjs;

/**
 * 用于extjs输出表头内容
 *
 * 网格列的元数据
 */
public class GroupingHeaderGridColumnMeta {

    private String header = "";
    private boolean hidden = false;
    private int width = 30;
    private boolean sortable = false;
    private boolean resizable = false;
    private String align = "left";
    private String dataIndex;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
        if (this.header.length() == 0) {
            this.setWidth(0);
            this.setHidden(true);
        }
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

} // EOP
