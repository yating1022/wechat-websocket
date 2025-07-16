package com.fruits.wechatWebsocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fruits.wechatWebsocket.Entity.chatRecordEntity;

import java.util.List;

public interface chatRecordService extends IService<chatRecordEntity> {

    List<chatRecordEntity> findAllByUserId(Integer userId);

}
