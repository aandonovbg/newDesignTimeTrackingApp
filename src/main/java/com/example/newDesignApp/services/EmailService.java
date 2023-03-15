package com.example.newDesignApp.services;

import com.example.newDesignApp.dto.UserRegisterDto;
import com.example.newDesignApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String to, String subject, UserRegisterDto registerDto) throws MessagingException, IOException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            Resource resource = new ClassPathResource("/templates/registrationEmail.html");
            String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            html = html.replace("{{fullName}}", registerDto.getFullName())
                    .replace("{{username}}", registerDto.getUsername())
                    .replace("{{password}}", registerDto.getPassword());
            helper.setText(html, true);
            mailSender.send(message);


    }
    public void sendUpdateEmail(String to, String subject, User user, String password) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        Resource resource = new ClassPathResource("/templates/updateEmail.html");
        String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        html = html.replace("{{fullName}}", user.getFullName())
                .replace("{{username}}", user.getUsername())
                .replace("{{password}}", password);
        helper.setText(html, true);
        mailSender.send(message);
    }
}
