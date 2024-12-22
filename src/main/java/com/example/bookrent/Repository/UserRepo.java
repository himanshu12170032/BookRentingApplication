package com.example.bookrent.Repository;

import com.example.bookrent.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    public boolean existsByEmail(String email);
    User findByEmail(String email);
}
