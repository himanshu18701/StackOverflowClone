package com.himanshu.stackoverflow.controller;

import com.himanshu.stackoverflow.entity.Comment;
import com.himanshu.stackoverflow.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save-comments")
    public String saveComments(@RequestParam("questionId") int questionId, @RequestParam("answerId") int answerId, @ModelAttribute("newComment") Comment comment, Model model) {
        commentService.saveComment(answerId, comment);
        model.addAttribute("comment", comment);
        return "redirect:/questions/" + questionId;
    }
}
