package com.human.graduateproject.services;

import com.human.graduateproject.dto.SignupRequest;
import com.human.graduateproject.dto.UserDto;
import com.human.graduateproject.entity.Users;
import com.human.graduateproject.enums.UserRole;
import com.human.graduateproject.mapper.UserMapper;
import com.human.graduateproject.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Override
    public UserDto createUser(SignupRequest signupRequest) {

        // Kiểm tra email đã tồn tại hay chưa trước khi làm gì khác
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        Users users = new Users();


        users.setEmail(signupRequest.getEmail());
        users.setPassword(encoder.encode(signupRequest.getPassword()));
        users.setName(signupRequest.getName());
        users.setRole(UserRole.CUSTOMER);

        Users createdUser = userRepository.save(users);

        return UserMapper.mapToUserDto(createdUser);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @PostConstruct
    public void createAdminAccount() {
        if (!userRepository.existsByRole(UserRole.ADMIN)) {
            Users admin = new Users();
            admin.setEmail("admin@gmail.com");
            admin.setName("Admin");
            admin.setRole(UserRole.ADMIN);
            admin.setPassword(encoder.encode("admin123"));
            userRepository.save(admin);
        }
    }
}
