package com.himanshu.stackoverflow.repository;

import com.himanshu.stackoverflow.entity.QuestionComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCommentRepository extends JpaRepository<QuestionComment, Integer> {
}
