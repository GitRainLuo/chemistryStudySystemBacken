package com.chemistrystudysystem.api;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/1/31 11:13
 * @Version:1.0
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chemistrystudysystem.entity.User;
import com.chemistrystudysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/1/31 11:13
 * @Version:1.0
 */
@RestController
@RequestMapping("user")
public class UserServiceApi {
    /*
    * userService
    **/
    @Autowired
    private UserService userService;

    /*
    * 用户注册
    **/
    @PostMapping("/register")
    public JSONObject register(@RequestBody User user){
        return userService.register(user);
    }

    /*
    * 用户登录
    **/
    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject params){
        return userService.login(params);
    }
}
