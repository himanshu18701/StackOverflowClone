package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.repository.UserQuestionRepository;
import com.himanshu.stackoverflow.entity.UserQuestion;
import org.springframework.stereotype.Service;

@Service
public class UserQuestionServiceImpl implements UserQuestionService {
    private final UserQuestionRepository userQuestionRepository;

    public UserQuestionServiceImpl(UserQuestionRepository userQuestionRepository) {
        this.userQuestionRepository = userQuestionRepository;
    }

    @Override
    public UserQuestion findUserVotedQuestionById(int userId, int questionId) {
        return userQuestionRepository.findUserQuestionByUserIdAndQuestionId(userId, questionId);
    }

    @Override
    public void saveUserVotedQuestion(UserQuestion userQuestion) {
        userQuestionRepository.save(userQuestion);
    }
}
