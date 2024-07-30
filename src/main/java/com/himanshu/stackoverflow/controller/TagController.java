package com.himanshu.stackoverflow.controller;

import com.himanshu.stackoverflow.entity.Tag;
import com.himanshu.stackoverflow.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    public String getAllTags(Model model) {
        List<Tag> tags = tagService.findAllTags();
        List<Tag> latestTags = new ArrayList<>();
        for (Tag tag : tags) {
            if (tag.getName().length() > 1 && !(tag.getQuestions().size() == 0)) {
                latestTags.add(tag);
            }
        }
        model.addAttribute("tags", latestTags);
        return "tags-page";
    }

    @GetMapping("/{id}")
    public String getTagById(@PathVariable("id") int id, Model model) {
        Tag tag = tagService.findTagById(id);
        model.addAttribute("questions", tag.getQuestions());
        return "tag-detail";
    }

    @GetMapping("/filter")
    public String filterTags(Model model, @RequestParam("tagName") String tagName) {
        List<Tag> tags = tagService.findAllTagsByTagName(tagName);
        model.addAttribute("tags", tags);
        return "tags-page";
    }
}
