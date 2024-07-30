package com.himanshu.stackoverflow.controller;

import com.himanshu.stackoverflow.auxiliary.QuestionHelper;
import com.himanshu.stackoverflow.auxiliary.Sort;
import com.himanshu.stackoverflow.entity.*;
import com.himanshu.stackoverflow.service.*;
import com.mountblue.stackoverflow.entity.*;
import com.mountblue.stackoverflow.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final CommentService commentService;
    private final AnswerService answerService;
    private final TagService tagService;
    private final UserService userService;
    private final UserQuestionService userQuestionService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public QuestionController(QuestionService questionService, AnswerService answerService, CommentService commentService, TagService tagService, UserService userService, UserQuestionService userQuestionService, CloudinaryService cloudinaryService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.userService = userService;
        this.userQuestionService = userQuestionService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Question> questions = questionService.findTodayQuestions();
        for (Question question : questions) {
            List<Tag> tags = tagService.findAllTagsByQuestionId(question.getId());
            question.setTags(tags);
        }
        questionService.setTimeAgo(questions);
        model.addAttribute("questions", questions);
        model.addAttribute("home", true);
        return "home";
    }


    @GetMapping("/list")
    public String FindAll(Model model, HttpServletRequest request) {
        String searchKeyword = request.getParameter("searchKeyword");
        String sortKeyword = request.getParameter("sortKeyword");
        String pNo = request.getParameter("pageNo");
        List<Question> questions;
        List<Question> questionsPerPage = new ArrayList<>();
        int pageNo = 0;
        int totalPages = 1;
        int pageSize = 15;

        searchKeyword = QuestionHelper.trimKeyword(searchKeyword);
        sortKeyword = QuestionHelper.trimKeyword(sortKeyword);
        questions = QuestionHelper.findQuestions(searchKeyword, sortKeyword, pageSize);

        totalPages = (int)Math.ceil(questions.size() * 1.0 / pageSize);

        if(pNo != null  &&  pNo.trim().length() > 0) {
            pageNo = Integer.parseInt(pNo);

            if(request.getParameter("page") != null) {
                pageNo++;
            } else {
                pageNo--;
            }
        }

        pageNo = Math.max(pageNo, 0);

        for(int i=0; i<pageSize; i++) {
            int index = pageNo * pageSize + i;
            if(index >= questions.size()) {
                break;
            }

            questionsPerPage.add(questions.get(index));
        }

        questionService.setTimeAgo(questionsPerPage);
        model.addAttribute("questions", questionsPerPage);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("home", false);
        model.addAttribute("sortKeyword", sortKeyword);
        return "home";
    }

    @GetMapping("/create")
    public String showQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("update", false);
        return "ask-question";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("question") Question question) {
        questionService.saveQuestion(question);
        return "redirect:/questions/home";
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
    @GetMapping("/{id}")
    public String showQuestion(@PathVariable("id") int id, Model model,@RequestParam(defaultValue = "3") int limit) {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String user = auth.getName();
        Question question = questionService.findQuestionById(id);
        List<Answer> answers = answerService.findAllAnswers(id);
        List<Answer> sortedAnswers = Sort.sort(answers);
        List<Tag> tags = tagService.findAllTagsByQuestionId(question.getId());
        List<QuestionComment> questionComments = question.getComments();
        Set<Question> relatedQuestions  = new HashSet<>();
        for(Answer answer : sortedAnswers) {
            List<Comment> allComments = commentService.findAllComments(answer.getId());
            List<Comment> commentsToDisplay = allComments.subList(0, Math.min(limit, allComments.size()));
            answer.setComments(commentsToDisplay);
            answer.setCommentSize(allComments.size());
        }
        for(Tag tag : tags) {
            List<Question> tagQuestions = questionService.findQuestionsByTagName(tag.getName());
            for (Question tagQuestion : tagQuestions) {
                relatedQuestions.add(tagQuestion);
            }
        }
        if(model.containsAttribute("updateAnswer")) {
            System.out.println(model.getAttribute("updateAnswer"));
        }

        model.addAttribute("question", question);
        model.addAttribute("questionComments", questionComments);
        model.addAttribute("answers", sortedAnswers);
        model.addAttribute("answer",new Answer());
        model.addAttribute("newAnswer", new Answer());
        model.addAttribute("questionComment",new QuestionComment());
        model.addAttribute("newComment", new Comment());
        model.addAttribute("tags", tags);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("loggedUserId",userService.getLoggedUserId());
        model.addAttribute("currentLimit",limit);
        model.addAttribute("user",user);
        return "question-page";
    }

    @GetMapping("/edit")
    public String editQuestion(@RequestParam("id") int id, Model model) {
        Question question = questionService.findQuestionById(id);
        StringBuilder tagStringBuilder = new StringBuilder();
        for(Tag tag : question.getTags()) {
            if(tagStringBuilder.length() > 0) {
                tagStringBuilder.append(",");
            }
            tagStringBuilder.append(tag.getName());
        }
        String tagString = tagStringBuilder.toString();
        question.setTagString(tagString);
        model.addAttribute("question", question);
        model.addAttribute("update", true);
        return "ask-question";
    }

    @PostMapping("/update")
    public String updateQuestion(@ModelAttribute("question") Question question, @RequestParam("id") int questionId) {
        List<Tag> tags = new ArrayList<>();
        for(String tag : question.getTagString().split(",")) {
            tags.add(new Tag(tag));
        }
        question.setTags(tags);
        Question exisitingQuestion=questionService.findQuestionById(questionId);
        question.setVotes(exisitingQuestion.getVotes());
        questionService.saveQuestion(question);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/delete")
    public String deleteQuestion(@RequestParam("id") int id) {
        questionService.deleteQuestionById(id);
        return "redirect:/questions/home";
    }

    @GetMapping("/vote")
    public String updateVote(@RequestParam("questionId") int questionId, @RequestParam("upvote") boolean upvote) {
        int userId = userService.getLoggedUserId();
        UserQuestion userQuestion = userQuestionService.findUserVotedQuestionById(userId, questionId);
        User user = userService.findById(userId);
        Question question = questionService.findQuestionById(questionId);

        if(userQuestion == null) {
            userQuestion = new UserQuestion(user, question, true);
        }

        if(upvote) {
            userQuestion.setVoted(true);
        } else {
            userQuestion.setVoted(false);
        }

        userQuestionService.saveUserVotedQuestion(userQuestion);
        questionService.updateVotes(questionId);
        return "redirect:/questions/" + questionId;
    }
}