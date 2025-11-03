package com.human.graduateproject.config;

import com.human.graduateproject.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.core.annotation.Order;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private JwtFilter jwtFilter;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter,CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    @Bean
    @Order(0)
    public SecurityFilterChain webhookChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/webhook/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var c = new org.springframework.web.cors.CorsConfiguration();
                    c.addAllowedOriginPattern("*");
                    c.addAllowedMethod("*");
                    c.addAllowedHeader("*");
                    c.setAllowCredentials(false);
                    return c;
                }))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable())
                .build();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(customer ->customer.disable())
                .authorizeHttpRequests(request->request
                        .requestMatchers("/authenticate","/reset/**","/api/file-upload","/sign-up","/vouchers/**","/cookie","/api/momo/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/reviews/product/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/reviews/product/*/stats").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/reviews/limit-product").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/customer/**").hasAnyRole("CUSTOMER","ADMIN")
                        .requestMatchers("/api/staff/**").hasAnyRole("STAFF","ADMIN")
                        .requestMatchers("/api/delivery/**").hasAnyRole("DELIVERY","ADMIN")
                        .requestMatchers("/api/order/**").hasAnyRole("STAFF","DELIVERY","ADMIN")
                        .requestMatchers("/api/products/**").permitAll()
                        .anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults())
//                .oauth2Login(oauth2 -> oauth2
//                        .successHandler(customOAuth2SuccessHandler))
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }




}
