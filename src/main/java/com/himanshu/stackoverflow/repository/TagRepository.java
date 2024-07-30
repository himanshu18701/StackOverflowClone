package com.himanshu.stackoverflow.repository;

import com.himanshu.stackoverflow.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String name);

    @Query("select t from Tag  t where lower(t.name) like lower(concat('%', :tagName,'%'))")
    List<Tag> findAllByName(String tagName);
}
