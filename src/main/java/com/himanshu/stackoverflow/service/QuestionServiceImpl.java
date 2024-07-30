package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.repository.QuestionRepository;
import com.himanshu.stackoverflow.repository.TagRepository;
import com.himanshu.stackoverflow.repository.UserQuestionRepository;
import com.himanshu.stackoverflow.repository.UserRepository;
import com.himanshu.stackoverflow.entity.Question;
import com.himanshu.stackoverflow.entity.Tag;
import com.himanshu.stackoverflow.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final UserQuestionRepository userQuestionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, TagRepository tagRepository, UserRepository userRepository, UserQuestionRepository userQuestionRepository) {
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.userQuestionRepository = userQuestionRepository;
    }

    @Override
    public Page<Question> findAllQuestion(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return questionRepository.findAll(pageable);
    }

    @Override
    public List<Question> findTodayQuestions() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return questionRepository.findQuestionsCreatedToday(startOfDay, endOfDay);
    }

    @Override
    public Question findQuestionById(int questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    @Override
    @Transactional
    public Question saveQuestion(Question question) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        question.setUser(user);

        String tagString = question.getTagString();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagString.split(",")) {
            tagName = tagName.trim();
            if (!tagName.isEmpty()) {
                Tag tag = tagRepository.findByName(tagName);
                if (tag == null) {
                    tag = tagRepository.save(new Tag(tagName));
                }
                tags.add(tag);
            }
        }
        question.setTags(tags);
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion;
    }

    @Override
    public void deleteQuestionById(int questionId) {
        questionRepository.deleteById(questionId);
    }


    @Override
    public List<Question> findQuestionsByTagName(String tagName) {
        return questionRepository.findQuestionsByTagName(tagName);
    }

    @Override
    public void setTimeAgo(List<Question> questions) {
        for (Question question : questions) {
            LocalDateTime createdAt = question.getCreatedAt();
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(createdAt, now);
            long days = duration.toDays();
            long hours = duration.toHours() % 24;
            long minutes = duration.toMinutes() % 60;
            String timeAgo;
            if (days > 0) {
                timeAgo = days + " days ago";
            } else if (hours > 0) {
                timeAgo = hours + " hours ago";
            } else {
                timeAgo = minutes + " minutes ago";
            }
            question.setTimeAgo(timeAgo);
        }
    }

    @Override
    public List<Question> findQuestionsSortByWithKeyword(String keyword, String sortBy, int pageNo, int pageSize) {
        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return questionRepository.findQuestionsSortByWithKeyword(keyword, pageable);
    }

    @Override
    public Page<Question> findQuestionsSortBy(String sortBy, int pageNo, int pageSize) {
        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return questionRepository.findAll(pageable);
    }

    @Override
    public List<Question> findQuestionsWithKeyword(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return questionRepository.findQuestionsWithKeyword(keyword, pageable);
    }

    @Override
    public void updateVotes(int questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        int votes = userQuestionRepository.getVotes(questionId, true);
        votes = votes - userQuestionRepository.getVotes(questionId, false);
        question.setVotes(votes);
        questionRepository.save(question);
    }
}
