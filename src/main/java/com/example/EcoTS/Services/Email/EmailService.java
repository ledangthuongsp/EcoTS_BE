package com.example.EcoTS.Services.Email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Gửi email với mật khẩu tạm thời
    public void sendTemporaryPassword(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Temporary Password");
        message.setText("Your temporary password is: " + password);
        mailSender.send(message);
    }
}
