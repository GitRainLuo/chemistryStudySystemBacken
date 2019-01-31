package com.chemistrystudysystem.service.impl;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/1/31 13:03
 * @Version:1.0
 */

import com.alibaba.fastjson.JSONObject;
import com.chemistrystudysystem.entity.User;
import com.chemistrystudysystem.repository.UserRepository;
import com.chemistrystudysystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/1/31 13:03
 * @Version:1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService{
    //用户仓库
    @Autowired
    private UserRepository userRepository;
    //注册
    @Override
    @Transactional
    public JSONObject register(User user){
        if(user == null){
            log.error("user不能为空!");
            try {
                throw new Exception("user为空!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JSONObject registerData = new JSONObject();
        JSONObject jsData = new JSONObject();
        //校验是否有账号了
        if(user.getAccount() != null){
            User user1 = userRepository.findByAccount(user.getAccount());
            if(user1 == null){
                jsData.put("code",0);
                jsData.put("msg","注册成功");
                registerData.put("data",jsData);
            }else if(user.getAccount().equals(user1.getAccount())){
                jsData.put("code",999);
                jsData.put("msg","账号已经存在");
                registerData.put("data",jsData);
            }
        }
        if("admin".equals(user.getAccount())){
            //管理员
            user.setUserType("admin");
        }
        userRepository.save(user);
        return registerData;
    }
    //登录
    public JSONObject login(JSONObject params){
        if(StringUtils.isEmpty(params)){
            log.error("登录信息为空");
            try {
                throw new Exception("登录信息不能为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JSONObject resData = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        log.info("account:"+params.get("account"));
        log.info("password:"+params.get("password"));
        if(params.get("account") != null){
            User user = userRepository.findByAccount(params.get("account").toString());
            if(user == null){
                log.error("用户不存在");
                jsonObject.put("code",999);
                jsonObject.put("msg","账号不存在");
                resData.put("data",jsonObject);
            }else {
                log.info(user.getAccount());
                log.info(user.getPassword());
                if(params.get("account").equals(user.getAccount())&&params.get("password").equals(user.getPassword())){
                    jsonObject.put("code",0);
                    jsonObject.put("msg","登陆成功");
                    jsonObject.put("user",user);
                    resData.put("data",jsonObject);
                }else {
                    jsonObject.put("code",999);
                    jsonObject.put("msg","账号或密码不正确");
                    resData.put("data",jsonObject);
                }
            }
        }
        return resData;
    }
}
