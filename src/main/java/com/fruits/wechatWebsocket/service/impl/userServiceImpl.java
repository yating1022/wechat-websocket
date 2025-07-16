package com.fruits.wechatWebsocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruits.wechatWebsocket.Entity.userEntity;
import com.fruits.wechatWebsocket.mapper.userMapper;
import com.fruits.wechatWebsocket.service.userService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class userServiceImpl extends ServiceImpl<userMapper, userEntity> implements userService {

    @Override
    public List<userEntity> getUserList() {
        return this.list();
    }

    @Override
    public userEntity getUserByChatName(String chatName) {
        LambdaQueryWrapper<userEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userEntity::getChatName, chatName);
        return this.getOne(wrapper);
    }

    @Override
    public userEntity getUserById(Integer userId) {
        LambdaQueryWrapper<userEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userEntity::getId, userId);
        userEntity one = this.getOne(wrapper);
        return one;
    }
}
