package com.example.quiz.dto;

public class AnswerMessage {

    private String user;
    private String answer;

    public AnswerMessage() {}

    public AnswerMessage(String user, String answer) {
        this.user = user;
        this.answer = answer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
