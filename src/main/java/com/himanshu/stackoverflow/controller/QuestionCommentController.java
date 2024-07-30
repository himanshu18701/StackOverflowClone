package com.himanshu.stackoverflow.controller;

import com.himanshu.stackoverflow.entity.QuestionComment;
import com.himanshu.stackoverflow.service.QuestionCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/question-comments")
public class QuestionCommentController {
    private final QuestionCommentService questionCommentService;

    @Autowired
    public QuestionCommentController(QuestionCommentService questionCommentService) {
        this.questionCommentService = questionCommentService;
    }

    @PostMapping("/save-question-comment")
    public String saveQuestionComment(@RequestParam("questionId") int questionId, @ModelAttribute("questionComment") QuestionComment comment) {
        questionCommentService.saveQuestionComment(questionId, comment);
        return "redirect:/questions/" + questionId;
    }
}
