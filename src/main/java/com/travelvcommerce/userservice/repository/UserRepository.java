package com.travelvcommerce.userservice.repository;

import com.travelvcommerce.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    void deleteByUserId(String userId);
    boolean existsByEmail(String email);
    Optional<User> findByUserId(String userId);
}
