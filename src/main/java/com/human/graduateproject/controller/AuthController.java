package com.human.graduateproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.human.graduateproject.dto.AuthenticationRequest;
import com.human.graduateproject.dto.SignupRequest;
import com.human.graduateproject.dto.UserDto;
import com.human.graduateproject.entity.PasswordResetToken;
import com.human.graduateproject.entity.Users;
import com.human.graduateproject.mapper.UserMapper;
import com.human.graduateproject.repository.PasswordRestTokenRepository;
import com.human.graduateproject.repository.UserRepository;
import com.human.graduateproject.services.AuthService;
import com.human.graduateproject.services.EmailService;
import com.human.graduateproject.services.PasswordResetTokenService;
import com.human.graduateproject.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING="Authorization";

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private  final JwtUtil jwtUtil;

    private final AuthService authService;

    private final PasswordResetTokenService passwordResetTokenService;

    private final EmailService emailService;

    private final PasswordRestTokenRepository passwordRestTokenRepository;

    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder(12);

    private ObjectMapper objectMapper = new ObjectMapper();

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                          UserRepository userRepository, JwtUtil jwtUtil,AuthService authService,
                          PasswordResetTokenService passwordResetTokenService, EmailService emailService,
                          PasswordRestTokenRepository passwordRestTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authService= authService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
        this.passwordRestTokenRepository = passwordRestTokenRepository;

    }

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws JSONException, IOException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        }
        catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password.");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
         Users users = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Email not found"));

        final String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject()
                    .put("userId",users.getId())
                    .put("role",users.getRole())
                    .put("name",users.getName())
                    .put("email",users.getEmail())
                    .put("picture",users.getImg())
                    .toString());

        System.out.println("tên user: "+ users.getName());

        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Expose-Headers","Authorization");
        response.addHeader("Access-Control-Expose-Headers","Refresh-Token");
        response.addHeader("Access-Control-Allow-Headers","Authorization, Refresh-Token, X-PINGOTHER, Origin, "+
                "X-Requested-With, Content-Type, Accept, X-Custom-header");

        response.addHeader(HEADER_STRING,TOKEN_PREFIX+accessToken);
        response.addHeader("Refresh-Token",refreshToken);

    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        if (authService.hasUserWithEmail(signupRequest.getEmail())){

            return new ResponseEntity<>(Collections.singletonMap("message","Email đã tồn tại"), HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto,HttpStatus.CREATED);
    }

    @GetMapping("/cookie")
    public ResponseEntity<?> getUserInfo(@CookieValue(value = "user_info", required = false) String encodedData) {
        if (encodedData == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cookie không tồn tại!");
        }

        try {
            String jsonData = URLDecoder.decode(encodedData, StandardCharsets.UTF_8);
            Map<String, String> userInfo = objectMapper.readValue(jsonData, new TypeReference<Map<String, String>>() {});
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi xử lý cookie!");
        }
    }

    @PostMapping("/reset/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) throws MessagingException {

        Optional<Users> users= userRepository.findByEmail(email);
        if (users.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("message","Email không tồn tại"));
        }
        else if ( users.get().getPassword() == null || users.get().getPassword().isEmpty()){
            String message = "Tài khoản của bạn sử dụng Google/Facebook để đăng nhập. Vui lòng tiếp tục đăng nhập bằng phương thức này.";
            return ResponseEntity.badRequest().body(Collections.singletonMap("message",message));
        }


        String token = passwordResetTokenService.generatePasswordRestToken(email);

        emailService.sendResetPasswordEmail(email,token);
        System.out.println("pwRsToken:"+token);
        return ResponseEntity.ok(Map.of("message", "Email đặt lại mật khẩu đã được gửi"));
    }

    @PostMapping("/reset/validate-reset-token")
    public ResponseEntity<?> validatePwRestToken(@RequestBody String token){
        Optional<PasswordResetToken> pwRsToken = passwordRestTokenRepository.findByToken(token);
        System.out.println("token : "+token);
        if(pwRsToken.isPresent()){
            System.out.println("token rspw: "+pwRsToken.get().getToken());
            System.out.println("token rspw: "+pwRsToken.get().getExpiryDate());
            System.out.println("Thời gian bây giờ: "+ Instant.now());
        }

        if (pwRsToken.isEmpty() || pwRsToken.get().getExpiryDate().isBefore(Instant.now())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message","Token không hợp lệ hoặc đã hết hạn"));
        }

        return ResponseEntity.ok(Map.of("message","Token hợp lệ"));
    }

    @PostMapping("/reset/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> request){

        String token = request.get("token");
        String newPassword = request.get("newPassword");

        Optional<PasswordResetToken> pwRsToken = passwordRestTokenRepository.findByToken(token);
        if (pwRsToken.isEmpty() || pwRsToken.get().getExpiryDate().isBefore(Instant.now())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message","Token không hợp lệ hoặc đã hết hạn"));
        }

        Optional<Users> users = userRepository.findByEmail(pwRsToken.get().getEmail());
        if (users.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("message","Email không tồn tại"));
        }

        Users updateUser = users.get();

        updateUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(updateUser);

        // xoa token de khoi bi ke gian loi dung
        passwordRestTokenRepository.delete(pwRsToken.get());

        return ResponseEntity.ok(Map.of("message","Đặt lại mật khẩu thành công") );
    }

    @PostMapping("/reset/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String,String> rfToken){
        String refreshToken = rfToken.get("refreshToken");

        if (refreshToken=="" || refreshToken == null){
            return ResponseEntity.status(403).body("Refresh token không hợp lệ.");
        }
        else {
            String username = jwtUtil.extractUsername(refreshToken);
            System.out.println("username: "+username);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(refreshToken,userDetails)){
                String newAccessToken = jwtUtil.generateAccessToken(username);
                return ResponseEntity.ok(Collections.singletonMap("accessToken",newAccessToken));
            }
            return ResponseEntity.status(403).body("Refesh token đã hết hạn hoặc không hợp lệ");
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        Optional<Users> users = userRepository.findById(id);
        if (users.isEmpty())
            return ResponseEntity.badRequest().body(Collections.singletonMap("message","Không tìm thấy user với id: "+id));

        UserDto userDto = UserMapper.mapToUserDto(users.get());
        return  ResponseEntity.ok(userDto);
    }


}
