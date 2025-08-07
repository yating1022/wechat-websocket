package com.fruits.wechatWebsocket.controller;

import com.fruits.wechatWebsocket.Entity.*;
import com.fruits.wechatWebsocket.service.MessageService;
import com.fruits.wechatWebsocket.service.chatRecordService;
import com.fruits.wechatWebsocket.service.userService;
import com.fruits.wechatWebsocket.utils.WeChatXmlToJsonConverter;
import com.fruits.wechatWebsocket.utils.WechatXmlParser;
import com.fruits.wechatWebsocket.wechatUtils.AesException;
import com.fruits.wechatWebsocket.wechatUtils.WXBizJsonMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Controller
@Slf4j
public class wechatCallbackController {
    @Autowired
    WXBizJsonMsgCrypt wxBizJsonMsgCrypt;
    @Autowired
    userService userservice;
    @Autowired
    chatRecordService chatrecordService;
    @Autowired
    MessageService messageService;


    @GetMapping("/wechat/callback")
    @ResponseBody
    public String wechatCallback(@RequestParam("msg_signature") String msg_signature,
                                  @RequestParam("timestamp") String timestamp,
                                  @RequestParam("nonce") String nonce,
                                  @RequestParam("echostr") String echostr

    ) throws AesException {
        log.info("接收到回调请求");
        log.info("msg_signature：{}",msg_signature);
        log.info("timestamp：{}",timestamp);
        log.info("nonce：{}",nonce);
        log.info("echostr：{}",echostr);
        String s = wxBizJsonMsgCrypt.VerifyURL(msg_signature, timestamp, nonce, echostr);
        log.info("解析出明文{}",s);
        return s;
    }



    @PostMapping(
            value = "/wechat/callback",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE}, // 允许 JSON/XML
            produces = MediaType.TEXT_PLAIN_VALUE // 返回纯文本
    )
    @ResponseBody
    public String Message(
            @RequestParam("msg_signature") String MsgSignature,
            @RequestParam("timestamp") String TimeStamp,
            @RequestParam("nonce") String Nonce,
            HttpServletRequest request // 改用 HttpServletRequest 直接获取原始数据
    ) throws Exception {
        // 手动读取原始请求体（避免 Spring 自动解析）
        String rawBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        log.info("原始请求数据: {}", rawBody);
        String s = WeChatXmlToJsonConverter.convertXmlToJson(rawBody);
        // 解密
        String decryptedMsg = wxBizJsonMsgCrypt.DecryptMsg(MsgSignature, TimeStamp, Nonce, s);
        log.info("解密后的消息: {}", decryptedMsg);
        WechatMessage message = WechatXmlParser.parse(decryptedMsg);
        //获得消息
        System.out.println(message);
        Boolean b = saveMessageToDataBase(message, 0);
        if (b) {
            log.info("消息已存入数据库");
        }else {
            log.error("消息存入数据库失败，可能会丢失");
        }
        //同时，还需要发送到前端





        return "success";
    }



    /**
     * 保存消息到数据库,并且发送消息
     * @return
     */
    public Boolean saveMessageToDataBase(WechatMessage messagem,Integer type){
        chatRecordEntity chatRecordEntity = new chatRecordEntity();
        chatRecordEntity.setType(type);
        if (messagem.getPicUrl() != null && !messagem.getPicUrl().equals("")) {
//            他是图片消息
            chatRecordEntity.setMsg(messagem.getPicUrl());
        }else{
            chatRecordEntity.setMsg(messagem.getContent());
        }

        userEntity user = userservice.getUserByChatName(messagem.getFromUserName());
        chatRecordEntity.setUserId(user.getId());
        messageService.sendToUser(user.getId(),chatRecordEntity);
        boolean save = chatrecordService.save(chatRecordEntity);
        return save;
    }

    /**
     * 保存消息到数据库,并且发送消息
     * @return
     */
    public Boolean saveMessageToDataBase(chatRecordEntity messagem) {
        boolean save = chatrecordService.save(messagem);
        return save;
    }









}
