package com.himanshu.stackoverflow.controller;


import com.himanshu.stackoverflow.auxiliary.Sort;
import com.himanshu.stackoverflow.entity.*;
import com.himanshu.stackoverflow.service.*;
import com.mountblue.stackoverflow.entity.*;
import com.mountblue.stackoverflow.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final TagService tagService;
    private final CommentService commentService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final UserAnswerService userAnswerService;


    @Autowired
    public AnswerController(AnswerService answerService, QuestionService questionService, TagService tagService, CommentService commentService, UserService userService, CloudinaryService cloudinaryService, UserAnswerService userAnswerService) {
        this.answerService = answerService;
        this.tagService = tagService;
        this.commentService = commentService;
        this.questionService = questionService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.userAnswerService = userAnswerService;
    }

    @PostMapping("/submit-answer")
    public String submitAnswer(@ModelAttribute("answer") Answer answer,
                               @RequestParam("questionId") int questionId,
                               RedirectAttributes redirectAttributes,Model model) {
        ResponseEntity<String>response=answerService.saveOrUpdateAnswer(answer, questionId);
        redirectAttributes.addAttribute("questionId", questionId);
        if(response.getStatusCode()==HttpStatus.OK){
            return "redirect:/questions/" + questionId;
        }else{
            model.addAttribute("questionId",questionId);
            return "plagiarism";
        }
    }

    @PostMapping("/upload-image")
    @ResponseBody
    public Map<String, Object> uploadImage(@RequestParam("upload") MultipartFile file) throws IOException {
        String imageUrl = cloudinaryService.uploadFile(file);
        Map<String, Object> response = new HashMap<>();
        response.put("uploaded", true);
        response.put("url", imageUrl);
        return response;
    }

    @PostMapping("/update-answer")
    public String updateAnswer(HttpServletRequest request, @ModelAttribute("answer") Answer answerToUpdate, Model model, @RequestParam(defaultValue = "3") int limit) {
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        int updateAnswerId = Integer.parseInt(request.getParameter("updateAnswerId"));
        String content = answerToUpdate.getContent();
        answerToUpdate = answerService.findAnswerById(updateAnswerId);
        answerToUpdate.setContent(content);
        ResponseEntity<String> response=answerService.saveOrUpdateAnswer(answerToUpdate, questionId);
        if(response.getStatusCode()==HttpStatus.OK){
            return "redirect:/questions/" + questionId;
        }else{
            model.addAttribute("questionId", questionId);
            model.addAttribute("updateAnswerId", updateAnswerId);
            model.addAttribute("answer", answerToUpdate);
            model.addAttribute("error", "Content is plagiarized. Please modify the content and try again.");
            return "redirect:/questions/" + questionId;
        }
    }

    @GetMapping("/edit")
    public String editAnswer(@RequestParam("questionId") int questionId, @RequestParam("answerId") int answerId, Model model) {
        Answer answer = answerService.findAnswerById(answerId);
        Question question = questionService.findQuestionById(questionId);
        List<Answer> answers = answerService.findAllAnswers(questionId);
        List<Answer> sortedAnswers = Sort.sort(answers);
        List<Tag> tags = tagService.findAllTagsByQuestionId(questionId);
        Set<Question> relatedQuestions = new HashSet<>();
        for (Answer ans : sortedAnswers) {
            List<Comment> comments = commentService.findAllComments(ans.getId());
            ans.setComments(comments);
        }
        for (Tag tag : tags) {
            List<Question> tagQuestions = questionService.findQuestionsByTagName(tag.getName());
            for (Question tagQuestion : tagQuestions) {
                relatedQuestions.add(tagQuestion);
            }
        }
        model.addAttribute("question", question);
        model.addAttribute("answers", sortedAnswers);
        model.addAttribute("answer", answer);
        model.addAttribute("newComment", new Comment());
        model.addAttribute("tags", tags);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("answerId", answerId);
        model.addAttribute("questionComments", question.getComments());
        model.addAttribute("questionComment", new QuestionComment());
        model.addAttribute("currentLimit", 3);
        return "question-page";
    }

    @GetMapping("/delete")
    public String deleteAnswer(@RequestParam("questionId") int questionId, @RequestParam("answerId") int answerId) {
        answerService.deleteAnswer(answerId);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/vote")
    public String updateVote(@RequestParam("questionId") int questionId, @RequestParam("upvote") boolean upvote, @RequestParam("answerId") int answerId) {
        int userId = userService.getLoggedUserId();
        UserAnswer userAnswer = userAnswerService.findUserVotedAnswerById(userId, answerId);
        User user = userService.findById(userId);
        Answer answer = answerService.findAnswerById(answerId);

        if (userAnswer == null) {
            userAnswer = new UserAnswer(user, answer, true);
        }

        if (upvote) {
            userAnswer.setVoted(true);
        } else {
            userAnswer.setVoted(false);
        }
        userAnswerService.saveUserVotedAnswer(userAnswer);
        answerService.updateVotes(answerId);
        return "redirect:/questions/" + questionId;
    }
}