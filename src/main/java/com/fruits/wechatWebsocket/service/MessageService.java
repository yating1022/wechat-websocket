package com.fruits.wechatWebsocket.service;

import com.fruits.wechatWebsocket.Entity.chatRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 推送消息到指定用户
     * @param userId 对方用户id
     * @param message 消息内容
     */
    public void sendToUser(Integer userId, chatRecordEntity message) {
        // 推送到 /topic/message/{userId}
        messagingTemplate.convertAndSend("/topic/message/" + userId, message);
    }
}