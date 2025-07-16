package com.fruits.wechatWebsocket.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("chat_record")
@Data
public class chatRecordEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer type;
    private String msg;
}
