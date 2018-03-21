package com.sxh.service.content.api;

import com.sxh.common.utils.E3Result;
import com.sxh.common.utils.EasyUITreeResponse;

import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
public interface ContentCategoryService {
    public List<EasyUITreeResponse> list(Long id);

    E3Result save(Long parentId, String name);
}
