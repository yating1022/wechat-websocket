package com.fruits.wechatWebsocket.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fruits.wechatWebsocket.Entity.userEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface userService extends IService<userEntity> {

    List<userEntity> getUserList();
    userEntity getUserByChatName(String chatName);
    userEntity getUserById(Integer userId);


}
