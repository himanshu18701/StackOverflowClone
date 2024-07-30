package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAllTags();

    List<Tag> findAllTagsByQuestionId(int questionId);

    Tag findTagById(int tagId);

    List<Tag> findAllTagsByTagName(String tagName);
}
