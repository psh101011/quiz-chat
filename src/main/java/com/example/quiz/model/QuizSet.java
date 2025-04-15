package com.example.quiz.model;

import com.example.quiz.model.Question;
import java.util.List;

public class QuizSet {
    private String quizId;
    private String title;
    private List<Question> questions;

    public QuizSet(String quizId, String title, List<Question> questions) {
        this.quizId = quizId;
        this.title = title;
        this.questions = questions;
    }

    public String getQuizId() { return quizId; }
    public String getTitle() { return title; }
    public List<Question> getQuestions() { return questions; }
}
