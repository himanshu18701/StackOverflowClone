package com.himanshu.stackoverflow.service;


import com.himanshu.stackoverflow.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllComments(int answerId);

    void saveComment(int answerId, Comment comment);
}
