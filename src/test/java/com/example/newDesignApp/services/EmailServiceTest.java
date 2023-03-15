package com.example.newDesignApp.services;

import com.example.newDesignApp.dto.UserRegisterDto;
import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.User;
import com.example.newDesignApp.enums.Role;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.util.ArrayList;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EmailService.class})
@ExtendWith(SpringExtension.class)
class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void testSendRegistrationEmail() throws IOException, MessagingException, MailException {
        doNothing().when(javaMailSender).send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        UserRegisterDto userRegisterDto = new UserRegisterDto("aandonov", "Anton Andonov", "xxx@xxx.bg",
                "1234", Role.EMPLOYEE, true,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), "10000", "boss", "GSM");
        userRegisterDto.setFullName("Todor Andonov");
        emailService.sendRegistrationEmail("abv@abv.bg", "Zdr",
                userRegisterDto);
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send((MimeMessage) any());
    }


    @Test
    void testSendUpdateEmail() throws IOException, MessagingException, MailException {
        doNothing().when(javaMailSender).send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Employee employee = new Employee();
        employee.setAddress("Цар Осв");
        employee.setCity("Тарго");
        employee.setDailyProtocol(new ArrayList<>());
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Anton Andonov");
        employee.setGSM("UTF-8");
        employee.setId(1L);
        employee.setJobTitle("boss");
        employee.setPassword("1234");
        employee.setProfilePicture(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("UTF-8");
        employee.setUsername("aandonov");
        emailService.sendUpdateEmail("abv@abv.bg", "zdr", employee, "1234");
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send((MimeMessage) any());
    }
}

