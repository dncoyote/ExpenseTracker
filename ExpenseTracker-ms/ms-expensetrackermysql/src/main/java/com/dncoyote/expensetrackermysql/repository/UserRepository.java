package com.dncoyote.expensetrackermysql.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dncoyote.expensetrackermysql.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

}
