package com.sxh.service.goods.api;

import com.sxh.common.utils.EasyUITreeResponse;

import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
public interface ItemCategoryService {
    //返回easyui-tree要求的格式数据
    //不直接查询所有数据，而是根据父亲节点，查询，实现懒加载的效果
    public List<EasyUITreeResponse> listTree(Long pid);

}
