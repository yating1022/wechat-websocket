package com.fruits.wechatWebsocket.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class WeChatXmlToJsonConverter {

    public static String convertXmlToJson(String xml) throws Exception {
        // 解析XML字符串
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();

        // 提取XML中的关键字段
        String toUserName = root.elementTextTrim("ToUserName");
        String agentId = root.elementTextTrim("AgentID");
        String encrypt = root.elementTextTrim("Encrypt");

        // 构建目标JSON字符串
        return String.format(
                "{\"tousername\":\"%s\",\"encrypt\":\"%s\",\"agentid\":\"%s\"}",
                toUserName, encrypt, agentId
        );
    }

    public static void main(String[] args) throws Exception {
        // 示例XML输入
        String xml = "<xml>"
                + "<ToUserName><![CDATA[toUser]]></ToUserName>"
                + "<AgentID><![CDATA[toAgentID]]></AgentID>"
                + "<Encrypt><![CDATA[msg_encrypt]]></Encrypt>"
                + "</xml>";

        // 转换并输出结果
        String jsonOutput = convertXmlToJson(xml);
        System.out.println(jsonOutput);
        // 输出: {"tousername":"toUser","encrypt":"msg_encrypt","agentid":"toAgentID"}
    }
}