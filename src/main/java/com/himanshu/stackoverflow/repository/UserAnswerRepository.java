package com.himanshu.stackoverflow.repository;

import com.himanshu.stackoverflow.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
    @Query("select ua from UserAnswer ua " +
            "where ua.user.id = :userId and ua.answer.id = :answerId")
    UserAnswer findUserAnswerByUserIdAndAnswerId(int userId, int answerId);

    @Query("select count(*) from UserAnswer ua " +
            "where ua.answer.id = :answerId and ua.voted = :voted")
    int getVotes(int answerId, boolean voted);
}
