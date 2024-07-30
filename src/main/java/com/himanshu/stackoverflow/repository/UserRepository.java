package com.himanshu.stackoverflow.repository;

import com.himanshu.stackoverflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    @Query("select u from User u where lower(u.name) like lower(concat('%',:userName,'%'))")
    List<User> searchAllUsers(String userName);
}
