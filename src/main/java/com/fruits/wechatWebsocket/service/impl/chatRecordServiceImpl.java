package com.fruits.wechatWebsocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruits.wechatWebsocket.Entity.chatRecordEntity;
import com.fruits.wechatWebsocket.mapper.chatRecordEntityMapper;
import com.fruits.wechatWebsocket.service.chatRecordService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class chatRecordServiceImpl extends ServiceImpl<chatRecordEntityMapper, chatRecordEntity> implements chatRecordService {


    @Override
    public List<chatRecordEntity> findAllByUserId(Integer userId) {
        LambdaQueryWrapper<chatRecordEntity> eq = new LambdaQueryWrapper<chatRecordEntity>().eq(chatRecordEntity::getUserId, userId);
        List<chatRecordEntity> list = this.list(eq);
        return list;
    }
}
