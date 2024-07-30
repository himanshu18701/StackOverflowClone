package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.repository.AnswerRepository;
import com.himanshu.stackoverflow.repository.CommentRepository;
import com.himanshu.stackoverflow.repository.UserRepository;
import com.himanshu.stackoverflow.entity.Answer;
import com.himanshu.stackoverflow.entity.Comment;
import com.himanshu.stackoverflow.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, AnswerRepository answerRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> findAllComments(int answerId) {
        Answer answer = answerRepository.findById(answerId).orElse(null);
        return answer.getComments();
    }

    @Override
    public void saveComment(int answerId, Comment comment) {
        Answer answer = answerRepository.findById(answerId).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        comment.setAnswer(answer);
        comment.setUser(user);
        List<Comment> comments = answer.getComments();
        comments.add(comment);
        answer.setComments(comments);
        answerRepository.save(answer);
    }
}
