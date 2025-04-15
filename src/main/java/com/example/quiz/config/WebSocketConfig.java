package main.java.com.example.quiz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 WebSocket 주소
        registry.addEndpoint("/quiz-ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJS 지원 (브라우저 호환성 ↑)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커가 메시지를 전달할 경로 설정
        config.enableSimpleBroker("/topic"); // 구독용 prefix
        config.setApplicationDestinationPrefixes("/app"); // 메시지 보낼 prefix
    }
}
