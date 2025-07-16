package com.fruits.wechatWebsocket.controller;

import com.fruits.wechatWebsocket.Entity.chatRecordEntity;
import com.fruits.wechatWebsocket.Entity.userEntity;
import com.fruits.wechatWebsocket.service.chatRecordService;
import com.fruits.wechatWebsocket.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class apiController {
    @Autowired
    chatRecordService chatrecordService;
    @Autowired
    userService userservice;
    @GetMapping("/chat-records/{userId}")
    public List<chatRecordEntity> chatRecords(@PathVariable Integer userId){
        List<chatRecordEntity> allByUserId = chatrecordService.findAllByUserId(userId);
        return allByUserId;
    }

    @GetMapping("/users")
    public List<userEntity> getUsers(){
        List<userEntity> userList = userservice.getUserList();
        return userList;
    }
}
