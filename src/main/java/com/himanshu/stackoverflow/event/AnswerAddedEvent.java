package com.himanshu.stackoverflow.event;

import com.himanshu.stackoverflow.entity.Question;
import com.himanshu.stackoverflow.entity.Answer;
import org.springframework.context.ApplicationEvent;

public class AnswerAddedEvent extends ApplicationEvent {

    private final Question question;
    private final Answer answer;

    public AnswerAddedEvent(Object source, Question question, Answer answer) {
        super(source);
        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswer() {
        return answer;
    }

}
