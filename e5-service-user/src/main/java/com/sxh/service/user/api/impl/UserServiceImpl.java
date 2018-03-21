package com.sxh.service.user.api.impl;

import com.sxh.common.utils.E3Result;
import com.sxh.service.user.api.UserService;
import com.sxh.service.user.mapper.TbUserMapper;
import com.sxh.service.user.pojo.TbUser;
import com.sxh.service.user.pojo.TbUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by HuaYu on 2018/2/23.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    TbUserMapper tbUserMapper;

    @Override
    public E3Result check(String paramValue, Integer type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //判断传入参数的类型
        if(type==1){//类型是1，则参数值是username
            criteria.andUsernameEqualTo(paramValue);
        }
        if(type==2){//类型是2，则参数值是phone
            criteria.andPhoneEqualTo(paramValue);
        }
        if(type==3){//类型是3，则参数值是email
            criteria.andEmailEqualTo(paramValue);
        }
        List<TbUser> users = tbUserMapper.selectByExample(example);
        if(users!=null && users.size()>0){
            return  E3Result.ok(false);//用户已经存在，表示不能注册了
        }
        return E3Result.ok(true);
    }

    @Override
    public E3Result register(TbUser user) {
        user.setCreated(new Date());
        user.setUpdated(new Date());
        tbUserMapper.insert(user);
        return E3Result.ok();
    }

    @Override
    public E3Result login(String username, String password) {
        //验证用户名和密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(password);
        List<TbUser> users = tbUserMapper.selectByExample(example);
        //如果users集合的个数大于0，就说明用户存在密码正确
        if(users!=null && !users.isEmpty()){
            //用户存在，登录成功
            return  E3Result.ok(users.get(0));
        }
        return null;
    }

}
