package com.himanshu.stackoverflow.service;


import com.himanshu.stackoverflow.entity.Question;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService {
    Page<Question> findAllQuestion(int pageNo, int pageSize);

    List<Question> findQuestionsWithKeyword(String keyword, int pageNo, int pageSize);

    Question findQuestionById(int questionId);

    Question saveQuestion(Question question);

    void deleteQuestionById(int questionId);

    List<Question> findQuestionsByTagName(String tagName);

    List<Question> findTodayQuestions();

    void setTimeAgo(List<Question> questions);

    Page<Question> findQuestionsSortBy(String sortBy, int pageNo, int pageSize);

    List<Question> findQuestionsSortByWithKeyword(String keyword, String sortBy, int pageNo, int pageSize);

    void updateVotes(int questionId);
}
