package com.example.quiz.controller;

import com.example.quiz.dto.AnswerMessage;
import com.example.quiz.model.Question;
import com.example.quiz.model.QuizSet;
import com.example.quiz.storage.QuizStorage;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class QuizController {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, String> roomToQuizMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> roomToIndexMap = new ConcurrentHashMap<>();

    public QuizController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/room/{roomId}/join")
    public void joinRoom(@DestinationVariable String roomId, @Payload Map<String, String> joinData) {
        String incomingQuizId = joinData.get("quizId");
        String existingQuizId = roomToQuizMap.get(roomId);

        if (existingQuizId != null && !existingQuizId.equals(incomingQuizId)) {
            messagingTemplate.convertAndSend("/topic/room/" + roomId + "/chat",
                "[시스템] ❌ 해당 방은 이미 다른 퀴즈로 시작되었습니다. (선택된 퀴즈: " + existingQuizId + ")");
            return;
        }

        roomToQuizMap.put(roomId, incomingQuizId);
        roomToIndexMap.putIfAbsent(roomId, 0);
    }

    @MessageMapping("/room/{roomId}/answer")
    public void answerInRoom(@DestinationVariable String roomId, @Payload AnswerMessage message) {
        String quizId = roomToQuizMap.get(roomId);
        QuizSet quizSet = QuizStorage.getQuizById(quizId);
        if (quizSet == null) return;

        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/chat",
                message.getUser() + ": " + message.getAnswer());

        List<Question> questions = quizSet.getQuestions();
        int currentIndex = roomToIndexMap.getOrDefault(roomId, 0);

        if (currentIndex < questions.size()) {
            Question current = questions.get(currentIndex);
            if (current.getAnswer().equalsIgnoreCase(message.getAnswer().trim())) {
                messagingTemplate.convertAndSend("/topic/room/" + roomId + "/correct",
                        message.getUser() + "님이 정답을 맞혔습니다!");
                roomToIndexMap.put(roomId, currentIndex + 1);
                messagingTemplate.convertAndSend("/topic/room/" + roomId + "/next", "NEXT");
            }
        }
    }

    @GetMapping("/api/quiz/{quizId}")
    @ResponseBody
    public List<Question> getQuiz(@PathVariable String quizId) {
        QuizSet quizSet = QuizStorage.getQuizById(quizId);
        return quizSet != null ? quizSet.getQuestions() : Collections.emptyList();
    }

    @GetMapping("/api/room/{roomId}/index")
    @ResponseBody
    public int getCurrentIndex(@PathVariable String roomId) {
        return roomToIndexMap.getOrDefault(roomId, 0);
    }
}
