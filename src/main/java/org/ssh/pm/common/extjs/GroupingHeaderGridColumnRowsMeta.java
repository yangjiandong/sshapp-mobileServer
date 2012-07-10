package org.ssh.pm.common.extjs;

/**
 * 分组网格列标题分组情况的元数据
 */
public class GroupingHeaderGridColumnRowsMeta {

    private String header;
    private int colspan;
    private String align = "center";
    private int width;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}