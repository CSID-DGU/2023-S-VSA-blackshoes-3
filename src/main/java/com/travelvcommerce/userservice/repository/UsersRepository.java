package com.travelvcommerce.userservice.repository;

import com.travelvcommerce.userservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);
    void deleteByUserId(String userId);
    boolean existsByEmail(String email);
    Optional<Users> findByUserId(String userId);

    @Modifying
    @Query("UPDATE Users u SET u.providerId = :providerId WHERE u.email = :email")
    void updateProviderId(String providerId, String email);
    @Modifying
    @Query("UPDATE Users u SET u.provider = :provider WHERE u.email = :email")
    void updateProvider(String provider, String email);

}
