package com.human.graduateproject.repository;

import com.human.graduateproject.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRestTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    void deleteByEmail(String email);

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByEmail(String email);
}
