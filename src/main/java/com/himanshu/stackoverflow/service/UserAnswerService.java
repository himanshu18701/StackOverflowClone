package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.entity.UserAnswer;

public interface UserAnswerService {
    UserAnswer findUserVotedAnswerById(int userId, int answerId);

    void saveUserVotedAnswer(UserAnswer userAnswer);
}
