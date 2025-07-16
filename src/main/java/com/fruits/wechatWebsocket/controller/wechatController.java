package com.fruits.wechatWebsocket.controller;


import com.fruits.wechatWebsocket.Entity.userEntity;
import com.fruits.wechatWebsocket.ResponEntity.SendMsgResponse;
import com.fruits.wechatWebsocket.service.userService;
import com.fruits.wechatWebsocket.utils.WechatSendMsg;
import com.fruits.wechatWebsocket.utils.WechatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wechat")
public class wechatController {
    @Autowired
    private userService userservice;

    //测试获取token
    @GetMapping("/test")
    public void getToken(){
        List<userEntity> userList = userservice.getUserList();
        System.out.println(userList);
    }




}
