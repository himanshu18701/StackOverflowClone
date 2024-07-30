package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.entity.Answer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnswerService {
    List<Answer> findAllAnswers(int questionId);

    Answer findAnswerById(int id);

    void deleteAnswer(int id);

    ResponseEntity<String> saveOrUpdateAnswer(Answer answer, int questionId);

    void updateVotes(int answerId);
}
