package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.repository.UserAnswerRepository;
import com.himanshu.stackoverflow.entity.UserAnswer;
import org.springframework.stereotype.Service;

@Service
public class UserAnswerServiceImpl implements UserAnswerService {
    private final UserAnswerRepository userAnswerRepository;

    public UserAnswerServiceImpl(UserAnswerRepository userAnswerRepository) {
        this.userAnswerRepository = userAnswerRepository;
    }

    @Override
    public UserAnswer findUserVotedAnswerById(int userId, int answerId) {
        return userAnswerRepository.findUserAnswerByUserIdAndAnswerId(userId, answerId);
    }

    @Override
    public void saveUserVotedAnswer(UserAnswer userAnswer) {
        userAnswerRepository.save(userAnswer);
    }
}
