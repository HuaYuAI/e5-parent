package com.sxh.common.utils;

import java.io.Serializable;

/**
 * Created by HuaYu on 2018/2/18.
 */
public class EasyUIDatagridRequest implements Serializable {
    private Integer page;//当前页
    private Integer rows;//每页多少条记录
    private String sort;//根据什么排序
    private String order;//升序 降序
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getRows() {
        return rows;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
}
