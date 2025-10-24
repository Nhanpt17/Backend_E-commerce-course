package com.human.graduateproject.services;

import org.springframework.transaction.annotation.Transactional;
import com.human.graduateproject.entity.PasswordResetToken;
import com.human.graduateproject.entity.Users;
import com.human.graduateproject.repository.PasswordRestTokenRepository;
import com.human.graduateproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {
    private final PasswordRestTokenRepository passwordRestTokenRepository;
    private final UserRepository userRepository;

    public PasswordResetTokenService(PasswordRestTokenRepository passwordRestTokenRepository, UserRepository userRepository) {
        this.passwordRestTokenRepository = passwordRestTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String generatePasswordRestToken(String email){
        Users users = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Không tìm thấy người dùng với email: " + email));

        // xoa token rest password cu neu co
        Optional<PasswordResetToken> passwordResetToken12 = passwordRestTokenRepository.findByEmail(email);
        if(passwordResetToken12.isPresent()){
            passwordRestTokenRepository.deleteByEmail(email);
        }


        //tao token moi
        String pwResetToken = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusSeconds(15*60); // het han sau 15p

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(pwResetToken);
        passwordResetToken.setEmail(email);
        passwordResetToken.setExpiryDate(expiryDate);
        passwordRestTokenRepository.save(passwordResetToken);

        return pwResetToken;



    }
}
