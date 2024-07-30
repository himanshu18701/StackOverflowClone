package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    User findById(int id);

    User registerUser(User user);

    List<User> findUsersByName(String userName);

    Integer getLoggedUserId();

    User getLoggedUser();
}
