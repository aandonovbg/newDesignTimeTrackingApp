package com.example.newDesignApp.repository;


import com.example.newDesignApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public User getUserByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username <> :username")
    public List<User> getAllUsersExcludingUsername(@Param("username") String username);
}
