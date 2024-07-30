package com.himanshu.stackoverflow.repository;

import com.himanshu.stackoverflow.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Integer> {
    @Query("select uq from UserQuestion uq " +
            "where uq.user.id = :userId and uq.question.id = :questionId")
    UserQuestion findUserQuestionByUserIdAndQuestionId(int userId, int questionId);

    @Query("select count(*) from UserQuestion uq " +
            "where uq.question.id = :questionId and uq.voted = :voted")
    int getVotes(int questionId, boolean voted);
}

