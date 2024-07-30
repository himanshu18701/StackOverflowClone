package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.repository.UserRepository;
import com.himanshu.stackoverflow.entity.Answer;
import com.himanshu.stackoverflow.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AnswerService answerService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        User user = userRepository.findById(id).orElse(null);
        int tagScore = 0;
        int userReputation = 0;
        if (user != null) {
            List<Answer> answers = user.getAnswers();
            for (Answer answer : answers) {
                userReputation += answer.getVotes();
            }
            user.setReputation(userReputation);
        }
        return user;
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return null;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> findUsersByName(String name) {
        return userRepository.searchAllUsers(name);
    }

    @Override
    public Integer getLoggedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        if (user == null) {
            return 0;
        }
        return user.getId();
    }

    @Override
    public User getLoggedUser(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName());
    }
}
