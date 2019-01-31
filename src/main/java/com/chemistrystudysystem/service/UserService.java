package com.chemistrystudysystem.service;

import com.alibaba.fastjson.JSONObject;
import com.chemistrystudysystem.entity.User;

/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/1/31 11:45
 * @Version:1.0
 */
public interface UserService {
    //用户注册
    JSONObject register(User user);
    //用户登录
    JSONObject login(JSONObject params);
}
