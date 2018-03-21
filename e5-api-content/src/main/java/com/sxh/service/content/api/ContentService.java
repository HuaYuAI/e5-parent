package com.sxh.service.content.api;

import com.sxh.common.utils.E3Result;
import com.sxh.common.utils.EasyUIDatagridRequest;
import com.sxh.common.utils.EasyUIDatagridResponse;
import com.sxh.common.utils.EasyUITreeResponse;
import com.sxh.service.content.pojo.Content;

import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
public interface ContentService {
    public E3Result save(Content content);
    //查询版块内容，根据版块id
    List<Content> listByCatId(long l);

    EasyUIDatagridResponse list(long categoryId, EasyUIDatagridRequest es);
}
