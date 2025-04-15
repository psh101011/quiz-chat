package com.example.quiz.storage;

import com.example.quiz.model.Question;
import com.example.quiz.model.QuizSet;
import java.util.*;

public class QuizStorage {
    // quizId → QuizSet
    private static final Map<String, QuizSet> quizMap = new HashMap<>();

    static {
        quizMap.put("quiz-001", new QuizSet(
                "quiz-001",
                "과일 이름 영어로",
                List.of(
                        new Question("사과를 영어로 하면?", "apple"),
                        new Question("바나나를 영어로 하면?", "banana"),
                        new Question("포도를 영어로 하면?", "grape")
                )
        ));

        quizMap.put("quiz-002", new QuizSet(
                "quiz-002",
                "동물 이름 영어로",
                List.of(
                        new Question("고양이를 영어로 하면?", "cat"),
                        new Question("강아지를 영어로 하면?", "dog"),
                        new Question("토끼를 영어로 하면?", "rabbit")
                )
        ));
    }

    public static QuizSet getQuizById(String quizId) {
        return quizMap.get(quizId);
    }

    public static Set<String> getAllQuizIds() {
        return quizMap.keySet();
    }

    public static Collection<QuizSet> getAllQuizSets() {
        return quizMap.values();
    }
}
