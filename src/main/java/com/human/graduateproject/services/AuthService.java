package com.human.graduateproject.services;

import com.human.graduateproject.dto.SignupRequest;
import com.human.graduateproject.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
    void createAdminAccount();
}
