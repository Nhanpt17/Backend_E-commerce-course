package com.human.graduateproject.services;

import com.human.graduateproject.entity.Users;
import com.human.graduateproject.enums.UserRole;
import com.human.graduateproject.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Users users = userRepository.findByEmail(username).orElseThrow(()-> new RuntimeException("Email not found"));

        // Chuyển đổi role thành GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+users.getRole()));

        return  org.springframework.security.core.userdetails.User
                .withUsername(users.getEmail())
                .password(users.getPassword() != null ? users.getPassword() : "{noop}")
                .authorities(authorities)
                .build();
    }
}
