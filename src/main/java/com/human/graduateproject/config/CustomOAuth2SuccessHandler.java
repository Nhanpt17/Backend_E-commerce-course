package com.human.graduateproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.human.graduateproject.entity.Users;
import com.human.graduateproject.enums.UserRole;
import com.human.graduateproject.repository.UserRepository;
import com.human.graduateproject.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {


    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    public CustomOAuth2SuccessHandler(ObjectMapper objectMapper, JwtUtil jwtUtil, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String facebookId;

        if (email == null){ // truong hop face book khong cap email
            facebookId = oAuth2User.getAttribute("id");
            email = "fb_"+facebookId+ "@gmail.com";
        }

        String accessToken = jwtUtil.generateAccessToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);
        Map<String, Object> responseData = new HashMap<>();

        Optional<Users> users = userRepository.findByEmail(email);

        if(users.isEmpty()){
            Users savedUser = new Users();
            savedUser.setName(name);
            savedUser.setEmail(email);
            savedUser.setRole(UserRole.CUSTOMER);
            userRepository.save(savedUser);
            responseData.put("userId",savedUser.getId());
            responseData.put("role",savedUser.getRole());
        }
        else {
            responseData.put("userId",users.get().getId());
            responseData.put("role",users.get().getRole());
        }



        responseData.put("email", email);
        responseData.put("name", name);
        responseData.put("picture", picture);
        responseData.put("accessToken", accessToken);
        responseData.put("refreshToken", refreshToken);

        response.setContentType("text/html");
        response.getWriter().write("""
        <script>
            window.opener.postMessage({ type: 'LOGIN_SUCCESS' }, '*');
            window.close();
        </script>
        """);
        
        // Lưu JWT vào HTTP-Only Cookie


        String jsonData = objectMapper.writeValueAsString(responseData);
        // Encode để tránh lỗi khi lưu cookie
        String encodedData = URLEncoder.encode(jsonData, StandardCharsets.UTF_8);

        Cookie cookie = new Cookie("user_info", encodedData);
        cookie.setHttpOnly(true);  // Bảo vệ cookie khỏi JavaScript
        /*them de delpy*/

        cookie.setMaxAge(24 * 60 * 60);

        String serverName = request.getServerName();
        boolean isProduction = !serverName.contains("localhost");
        if (isProduction) {

            // Hack để thêm SameSite=None khi không có ResponseCookie:
            // Cấu hình cookie chặt chẽ hơn
            cookie.setSecure(true);  // Bật lại secure
            cookie.setAttribute("SameSite", "None");  // Quan trọng cho cross-site

        }
        /*them de deplou*/
        //cookie.setSecure(true);     // Chỉ gửi qua HTTPS
        cookie.setPath("/");        // Có thể truy cập từ mọi API


        response.addCookie(cookie);




    }
}
