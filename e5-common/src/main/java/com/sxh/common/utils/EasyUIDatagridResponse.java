package com.sxh.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
public class EasyUIDatagridResponse implements Serializable {
    private Long total;//总条数
    private List<?> rows;//记录数
    private List<?> footer;//合计

    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public List<?> getRows() {
        return rows;
    }
    public void setRows(List<?> rows) {
        this.rows = rows;
    }
    public List<?> getFooter() {
        return footer;
    }
    public void setFooter(List<?> footer) {
        this.footer = footer;
    }
}
