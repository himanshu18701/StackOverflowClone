package com.himanshu.stackoverflow.repository;

import com.himanshu.stackoverflow.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("Select q from Question q left join q.tags t " +
            "where lower(q.title) like %:keyword% " +
            "or lower(q.content) like %:keyword% " +
            "or lower(t.name) like %:keyword% " +
            "or exists (select 1 from Answer a " +
            "where a.question.id = q.id " +
            "and lower(a.content) like %:keyword%)")
    List<Question> findQuestionsWithKeyword(String keyword, Pageable pageable);

    @Query("Select q from Question q left join q.tags t " +
            "where lower(q.title) like %:keyword% " +
            "or lower(q.content) like %:keyword% " +
            "or lower(t.name) like %:keyword% " +
            "or exists (select 1 from Answer a " +
            "where a.question.id = q.id " +
            "and lower(a.content) like %:keyword%)")
    List<Question> findQuestionsSortByWithKeyword(String keyword, Pageable pageable);


    @Query("select q from Question q JOIN q.tags t where lower(t.name) = lower(:tagName) ")
    List<Question> findQuestionsByTagName(String tagName);


    @Query("SELECT q FROM Question q WHERE q.createdAt >= :startOfDay AND q.createdAt < :endOfDay")
    List<Question> findQuestionsCreatedToday(@Param("startOfDay") LocalDateTime startOfDay,
                                             @Param("endOfDay") LocalDateTime endOfDay);
}
