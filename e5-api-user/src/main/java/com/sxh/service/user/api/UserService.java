package com.sxh.service.user.api;

import com.sxh.common.utils.E3Result;
import com.sxh.service.user.pojo.TbUser;

/**
 * Created by HuaYu on 2018/2/23.
 */
public interface UserService {
    public E3Result check(String paramValue, Integer type);
    public E3Result register(TbUser user);
    public E3Result login(String username,String password);
}
