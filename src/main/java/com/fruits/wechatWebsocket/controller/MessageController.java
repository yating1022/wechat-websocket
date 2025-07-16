package com.fruits.wechatWebsocket.controller;
import com.fruits.wechatWebsocket.Entity.MessageRequest;
import com.fruits.wechatWebsocket.Entity.WechatMessage;
import com.fruits.wechatWebsocket.Entity.chatRecordEntity;
import com.fruits.wechatWebsocket.Entity.userEntity;
import com.fruits.wechatWebsocket.ResponEntity.SendMsgResponse;
import com.fruits.wechatWebsocket.service.userService;
import com.fruits.wechatWebsocket.utils.WechatSendMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
public class MessageController {

    @Autowired
    userService userservice;
    @Autowired
    WechatSendMsg wechatSendMsg;
    @Autowired
    wechatCallbackController  wechatCallback;

    // 处理发送到/app/message的消息
    @MessageMapping("/message")
    public String handleMessage(@RequestBody MessageRequest message) {
        System.out.println("前端消息为" + message);
        userEntity userById = userservice.getUserById(message.getUserId());
        SendMsgResponse sendMsgResponse = wechatSendMsg.SendMessage(userById.getChatName(), message.getMsg());
        Integer errcode = sendMsgResponse.getErrcode();
        log.info("状态码：{}", errcode);
        log.info("开始保存记录到数据库");
        chatRecordEntity chatRecordEntity = new chatRecordEntity();
        chatRecordEntity.setType(1);
        chatRecordEntity.setMsg(message.getMsg());
        chatRecordEntity.setUserId(Math.toIntExact(message.getUserId()));
        Boolean b = wechatCallback.saveMessageToDataBase(chatRecordEntity);
        if (b){
            log.info("消息保存到数据库成功");
        }else{
            log.error("消息保存到数据库失败，可能丢失数据");
        }
        return "服务器回应: " + message;
    }




}
