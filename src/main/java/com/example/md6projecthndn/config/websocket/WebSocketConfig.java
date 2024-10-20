package com.example.md6projecthndn.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Sử dụng /chat để làm điểm đến cho tin nhắn
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/api/chat");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Kết nối WebSocket sẽ qua endpoint này
        registry.addEndpoint("/api/chat/chat-websocket").setAllowedOrigins("*").withSockJS();
    }
}
