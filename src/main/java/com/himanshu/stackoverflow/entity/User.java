package com.himanshu.stackoverflow.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "skills")
    private String skills;

    @Column(name = "about")
    private String about;

    @Column(name = "reputation")
    private int reputation;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<QuestionComment> questionComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserQuestion> userVotedQuestions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAnswer> userVotedAnswers;

    public User() {
    }

    public User(int id, String name, String email, String password, String skills, List<Question> questions, List<Answer> answers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.skills = skills;
        this.questions = questions;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<QuestionComment> getQuestionComments() {
        return questionComments;
    }

    public void setQuestionComments(List<QuestionComment> questionComments) {
        this.questionComments = questionComments;
    }

    public List<UserQuestion> getUserVotedQuestions() {
        return userVotedQuestions;
    }

    public void setUserVotedQuestions(List<UserQuestion> userVotedQuestions) {
        this.userVotedQuestions = userVotedQuestions;
    }

    public List<UserAnswer> getUserVotedAnswers() {
        return userVotedAnswers;
    }

    public void setUserVotedAnswers(List<UserAnswer> userVotedAnswers) {
        this.userVotedAnswers = userVotedAnswers;
    }
}
