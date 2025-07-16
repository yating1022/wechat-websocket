package com.fruits.wechatWebsocket.ResponEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMsgResponse {
    private Integer errcode;
    private String errmsg;
    private String msgid;
}
