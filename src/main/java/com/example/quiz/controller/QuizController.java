package com.example.quiz.controller;

import com.example.quiz.dto.AnswerMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class QuizController {

    private final SimpMessagingTemplate messagingTemplate;

    public QuizController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/answer")
    public void receiveAnswer(@Payload AnswerMessage message) {
        String trimmed = message.getAnswer().trim();
    
        // ✅ 모든 유저에게 "누가 뭐라고 입력했는지" 먼저 알려주기
        messagingTemplate.convertAndSend("/topic/chat",
                message.getUser() + ": " + trimmed);
    
        // ✅ 정답 맞춘 경우에만 별도 메시지
        if ("apple".equalsIgnoreCase(trimmed)) {
            messagingTemplate.convertAndSend("/topic/correct",
                    message.getUser() + "님이 정답을 맞혔습니다!");
            messagingTemplate.convertAndSend("/topic/next", "NEXT");
        }
    }
}
