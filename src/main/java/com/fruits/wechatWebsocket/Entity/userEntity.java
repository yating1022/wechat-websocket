package com.fruits.wechatWebsocket.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class userEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String mobile;
    private String chatName;
}
