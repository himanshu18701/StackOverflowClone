package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.repository.QuestionRepository;
import com.himanshu.stackoverflow.repository.UserRepository;
import com.himanshu.stackoverflow.entity.Question;
import com.himanshu.stackoverflow.entity.QuestionComment;
import com.himanshu.stackoverflow.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionCommentServiceImpl implements QuestionCommentService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuestionCommentServiceImpl(QuestionRepository questionRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveQuestionComment(int questionId, QuestionComment questionComment) {
        Question question = questionRepository.findById(questionId).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        questionComment.setUser(user);
        questionComment.setQuestion(question);
        List<QuestionComment> comments = question.getComments();
        comments.add(questionComment);
        question.setComments(comments);
        questionRepository.save(question);
    }
}
