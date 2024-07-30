package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.repository.QuestionRepository;
import com.himanshu.stackoverflow.repository.TagRepository;
import com.himanshu.stackoverflow.entity.Question;
import com.himanshu.stackoverflow.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final QuestionRepository questionRepository;
    public TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(QuestionRepository questionRepository, TagRepository tagRepository) {
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> findAllTagsByQuestionId(int questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        List<Tag> tags = question.getTags();
        return tags;
    }

    @Override
    public Tag findTagById(int tagId) {
        return tagRepository.findById(tagId).orElse(null);
    }

    @Override
    public List<Tag> findAllTagsByTagName(String tagName) {
        return tagRepository.findAllByName(tagName);
    }


}
