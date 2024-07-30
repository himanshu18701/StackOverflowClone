package com.himanshu.stackoverflow.auxiliary;

import com.himanshu.stackoverflow.entity.Question;
import com.himanshu.stackoverflow.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionHelper {
    private static QuestionService questionService;

    @Autowired
    public QuestionHelper(QuestionService questionService) {
        this.questionService = questionService;
    }

    public static void removeOutdatedQuestions(List<Question> questions, String sortKeyword) {
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Duration duration = Duration.between(question.getCreatedAt(), LocalDateTime.now());
            long days = duration.toDays();

            if (sortKeyword.equals("month") && days > 30 || sortKeyword.equals("week") && days > 7) {
                questions.remove(i);
                i--;
            }
        }
    }

    public static String trimKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }

        keyword = keyword.trim();
        return (keyword.length() == 0) ? null : keyword;
    }

    public static String extractKeyword(String keyword) {
        String result = null;

        if (keyword == null) {
            return result;
        }

        if (!keyword.equals("top")) {
            if (keyword.equals("popularity")) {
                result = "votes";
            } else {
                result = "createdAt";
            }
        }

        return result;
    }

    public static List<Question> findQuestions(String searchKeyword, String sortKeyword, int pageSize) {
        List<Question> questions;
        List<Question> tempQuestions;
        Page<Question> questionsPage;
        String sortBy = extractKeyword(sortKeyword);

        if(searchKeyword != null) {
            searchKeyword = searchKeyword.toLowerCase();
        }

        if (sortKeyword == null) {
            if (searchKeyword == null) {
                questions = new ArrayList<>();

                for (int i = 0; true; i++) {
                    questionsPage = questionService.findAllQuestion(i, pageSize);
                    tempQuestions = questionsPage.getContent();

                    if (tempQuestions.size() == 0) {
                        break;
                    }

                    questions.addAll(tempQuestions);
                }
            } else {
                questions = new ArrayList<>();

                for (int i = 0; true; i++) {
                    tempQuestions = questionService.findQuestionsWithKeyword(searchKeyword, i, pageSize);

                    if (tempQuestions.size() == 0) {
                        break;
                    }

                    questions.addAll(tempQuestions);
                }
            }
        } else if (!sortKeyword.equals("top")) {
            if (searchKeyword == null) {
                questions = new ArrayList<>();

                for (int i = 0; true; i++) {
                    questionsPage = questionService.findQuestionsSortBy(sortBy, i, pageSize);
                    tempQuestions = questionsPage.getContent();

                    if (tempQuestions.size() == 0) {
                        break;
                    }

                    questions.addAll(tempQuestions);
                }
            } else {
                questions = new ArrayList<>();

                for (int i = 0; true; i++) {
                    tempQuestions = questionService.findQuestionsSortByWithKeyword(searchKeyword, sortBy, i, pageSize);

                    if (tempQuestions.size() == 0) {
                        break;
                    }

                    questions.addAll(tempQuestions);
                }
            }

            if (!sortBy.equals("votes")) {
                QuestionHelper.removeOutdatedQuestions(questions, sortKeyword);
            }
        } else {
            questions = new ArrayList<>();

            for (int i = 0; true; i++) {
                questionsPage = questionService.findQuestionsSortBy(sortBy, i, pageSize);
                tempQuestions = questionsPage.getContent();

                if (tempQuestions.size() == 0) {
                    break;
                }

                questions.addAll(tempQuestions);
            }
        }

        return questions;
    }
}
