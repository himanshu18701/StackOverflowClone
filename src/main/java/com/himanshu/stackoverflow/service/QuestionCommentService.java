package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.entity.QuestionComment;

public interface QuestionCommentService {
    void saveQuestionComment(int questionId, QuestionComment questionComment);
}
