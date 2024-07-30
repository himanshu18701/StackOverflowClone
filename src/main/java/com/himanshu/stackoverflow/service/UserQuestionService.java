package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.entity.UserQuestion;

public interface UserQuestionService {
    UserQuestion findUserVotedQuestionById(int userId, int questionId);

    void saveUserVotedQuestion(UserQuestion userQuestion);
}
