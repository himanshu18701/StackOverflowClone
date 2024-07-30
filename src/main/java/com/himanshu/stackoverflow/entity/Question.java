package com.himanshu.stackoverflow.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "votes")
    private int votes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionComment> comments;

    @ManyToMany
    @JoinTable(
            name = "question_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<UserQuestion> questionsVotedByUser;

    @Transient
    private String tagString;

    @Transient
    private String timeAgo;

    public Question() {
    }

    public Question(int id, String title, String content, LocalDateTime createdAt, int votes, User user, List<Answer> answers) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.votes = votes;
        this.user = user;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<QuestionComment> getComments() {
        return comments;
    }

    public void setComments(List<QuestionComment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public List<UserQuestion> getQuestionsVotedByUser() {
        return questionsVotedByUser;
    }

    public void setQuestionsVotedByUser(List<UserQuestion> questionsVotedByUser) {
        this.questionsVotedByUser = questionsVotedByUser;
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}