package com.sxh.service.content.api.impl;

import com.sxh.common.utils.E3Result;
import com.sxh.common.utils.EasyUITreeResponse;
import com.sxh.service.content.api.ContentCategoryService;
import com.sxh.service.content.mapper.ContentCategoryMapper;
import com.sxh.service.content.pojo.ContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
@Service("contentCategoryService")
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private ContentCategoryMapper contentCategoryMapper;
    @Override
    public  List<EasyUITreeResponse> list(Long pid) {

        //可以优化，修改sql语句，返回类型改为EasyUITreeResponse
        List<EasyUITreeResponse> contentCategories = contentCategoryMapper.selectByPid(pid);
        return contentCategories;
    }

    @Override
    public E3Result save(Long parentId, String name) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setIsParent(false);//新增的节点肯定是叶子节点
        contentCategory.setName(name);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        contentCategory.setParentId(parentId);
        contentCategoryMapper.insert(contentCategory);
        return E3Result.ok(contentCategory);
    }
}
