package com.fruits.wechatWebsocket.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 客户端连接地址
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // 允许所有来源，生产环境建议指定
                .withSockJS(); // 关键：启用SockJS支持

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 客户端接收地址前缀（服务端→客户端）
        registry.enableSimpleBroker("/topic");

        // 客户端发送地址前缀（客户端→服务端）
        registry.setApplicationDestinationPrefixes("/app");
    }
}