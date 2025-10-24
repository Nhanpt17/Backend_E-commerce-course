package com.human.graduateproject.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.client.url}")
    private String clientAppUrl ="";

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetPasswordEmail(String email, String token) throws MessagingException {
        String resetLink = clientAppUrl+"/reset-password?token="+token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("CoffeeMan - Đặt lại mật khẩu");

        String htmlContent = "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #333;'>Yêu cầu đặt lại mật khẩu</h2>" +
                "<p>Chào bạn,</p>" +
                "<p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản CoffeeMan của mình.</p>" +
                "<p>Vui lòng nhấp vào nút bên dưới để đặt lại mật khẩu:</p>" +
                "<p><a href='" + resetLink + "' " +
                "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #28a745; " +
                "text-decoration: none; border-radius: 5px;'>Đặt lại mật khẩu</a></p>" +
                "<p>Nếu bạn không yêu cầu thao tác này, vui lòng bỏ qua email này.</p>" +
                "<p>Trân trọng,<br>Đội ngũ CoffeeMan ☕</p>" +
                "</body></html>";

        helper.setText(htmlContent, true); // Chỉ định đây là email HTML

        mailSender.send(message);

    }

}
