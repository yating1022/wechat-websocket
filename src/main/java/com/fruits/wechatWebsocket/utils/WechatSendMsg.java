package com.fruits.wechatWebsocket.utils;


import com.fruits.wechatWebsocket.Entity.msgEntity;
import com.fruits.wechatWebsocket.Entity.textEntity;
import com.fruits.wechatWebsocket.ResponEntity.AccToken;
import com.fruits.wechatWebsocket.ResponEntity.SendMsgResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 发送消息的方法
 */
@Component
@Slf4j
public class WechatSendMsg {


    // 配置项（通过setter注入）
    private static String CORPID;
    private static String CORPSECRET;
    private static String PROXY_URL;
    private static Integer AGENTID;

    // RestTemplate单例
    private static final RestTemplate restTemplate = new RestTemplate();

    // Setter方法注入配置值
    @Value("${wx.corpid}")
    public void setCorpid(String corpid) {
        CORPID = corpid;
    }

    @Value("${wx.corpsecret}")
    public void setCorpsecret(String corpsecret) {
        CORPSECRET = corpsecret;
    }

    @Value("${wx.proxyUrl}")
    public void setProxyUrl(String proxyUrl) {
        PROXY_URL = proxyUrl;
    }
    @Value("${wx.agentid}")
    public void setAgentid(Integer agentid) {
        AGENTID = agentid;
    }


    /**
     * 发送消息方法
     * @param touser 发送对象
     * @param msg 发送内容
     * @return
     */
    public static SendMsgResponse SendMessage(String touser,String msg){
        msgEntity msgEntity = new msgEntity();
        msgEntity.setAgentid(AGENTID);
        msgEntity.setText(new textEntity(msg));
        msgEntity.setTouser(touser);
        String accessToken = WechatUtils.getAccessToken();
        String url = String.format("%s/cgi-bin/message/send?access_token=%s",
                PROXY_URL, accessToken);
        ResponseEntity<SendMsgResponse> sendMsgResponseResponseEntity = null;
        try {
            sendMsgResponseResponseEntity = restTemplate.postForEntity(url, msgEntity, SendMsgResponse.class);
            SendMsgResponse body = sendMsgResponseResponseEntity.getBody();
            return body;
        } catch (RestClientException e) {
            log.error("发送消息错误");
            throw new RuntimeException(e);
        }
    }



}
