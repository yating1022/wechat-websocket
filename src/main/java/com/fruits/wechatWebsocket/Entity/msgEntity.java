package com.fruits.wechatWebsocket.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class msgEntity {
    private String touser;
    private String msgtype = "text";
    private Integer agentid;
    private textEntity text;
}
