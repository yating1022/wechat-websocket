package com.fruits.wechatWebsocket.utils;

import com.fruits.wechatWebsocket.Entity.WechatMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class WechatXmlParser {

    public static WechatMessage parse(String xml) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new java.io.StringReader(xml));
        Element root = document.getRootElement();

        WechatMessage message = new WechatMessage();

        message.setToUserName(getElementText(root, "ToUserName"));
        message.setFromUserName(getElementText(root, "FromUserName"));
        message.setMsgType(getElementText(root, "MsgType"));
        message.setContent(getElementText(root, "Content"));
        message.setPicUrl(getElementText(root, "PicUrl"));

        // 处理数字类型字段
        message.setCreateTime(parseLong(getElementText(root, "CreateTime")));
        message.setMsgId(parseLong(getElementText(root, "MsgId")));
        message.setAgentID(parseInt(getElementText(root, "AgentID")));

        return message;
    }

    // 安全获取节点文本
    private static String getElementText(Element parent, String elementName) {
        Element elem = parent.element(elementName);
        return (elem != null) ? elem.getTextTrim() : null;
    }

    // 安全转换 Long
    private static Long parseLong(String value) {
        try {
            return (value != null) ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 安全转换 Integer
    private static Integer parseInt(String value) {
        try {
            return (value != null) ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}