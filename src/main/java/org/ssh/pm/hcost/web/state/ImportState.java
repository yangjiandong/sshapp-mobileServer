package org.ssh.pm.hcost.web.state;

import java.io.Serializable;

public class ImportState implements Serializable{

    private static final long serialVersionUID = -2725131221889480989L;

    Integer state;//状态，和操作状态一样
    Integer action;//继续操作还是停止
    Integer count;//总共归集项目项 -- 等待界面进度条
    Integer index;//当前完成项

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}

