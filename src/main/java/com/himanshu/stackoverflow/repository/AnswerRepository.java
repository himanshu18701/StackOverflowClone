package com.himanshu.stackoverflow.repository;

import com.himanshu.stackoverflow.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    @Query("select a from Answer a " +
            "where a.user.id = :userId")
    List<Answer> findAllAnswersByUserId(Integer userId);
}
