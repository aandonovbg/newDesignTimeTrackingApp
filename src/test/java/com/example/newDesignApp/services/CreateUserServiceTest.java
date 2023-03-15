package com.example.newDesignApp.services;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.newDesignApp.dto.UserRegisterDto;
import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.enums.Role;
import com.example.newDesignApp.repository.UserRepository;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.util.ArrayList;
import javax.mail.MessagingException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CreateUserService.class})
@ExtendWith(SpringExtension.class)
class CreateUserServiceTest {
    @Autowired
    private CreateUserService createUserService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testCreateUser() throws Exception {
        UserRegisterDto registerDto = new UserRegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("John Doe");
        registerDto.setUsername("johndoe");
        registerDto.setRole(Role.EMPLOYEE);
        registerDto.setEnabled(true);

        createUserService.createUser(registerDto);

        if (registerDto.getRole().equals(Role.ADMIN)) {
            Admin admin = new Admin();
            admin.setEmail(registerDto.getEmail());
            admin.setPassword(registerDto.getPassword());
            admin.setFullName(registerDto.getFullName());
            admin.setUsername(registerDto.getUsername());
            admin.setRole(registerDto.getRole());
            admin.setEnabled(registerDto.isEnabled());
            verify(userRepository).save(admin);
        } else {
            Employee employee = new Employee();
            employee.setEmail(registerDto.getEmail());
            employee.setPassword(registerDto.getPassword());
            employee.setFullName(registerDto.getFullName());
            employee.setUsername(registerDto.getUsername());
            employee.setRole(registerDto.getRole());
            employee.setEnabled(registerDto.isEnabled());
            verify(userRepository).save(employee);
        }

        String to = registerDto.getEmail();
        String subject = "Потвърждение за създаден акаунт!";
        verify(emailService).sendRegistrationEmail(to, subject, registerDto);
    }


    @Test
    void testCreateUser2() throws IOException, MessagingException {
        doNothing().when(emailService).sendRegistrationEmail((String) any(), (String) any(), (UserRegisterDto) any());

        Employee employee = new Employee();
        employee.setAddress("Tsar Osv");
        employee.setCity("Targo");
        employee.setDailyProtocol(new ArrayList<>());
        employee.setEmail("xxx@example.org");
        employee.setEnabled(true);
        employee.setFullName("Anton Andonov");
        employee.setGSM("+3595858554");
        employee.setId(1L);
        employee.setJobTitle("boss");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("4000");
        employee.setUsername("aandonov");
        when(userRepository.save((Employee) any())).thenReturn(employee);
        createUserService
                .createUser(new UserRegisterDto("aandonov", "Anton Andonov", "xxx@example.org", "1234", Role.EMPLOYEE,
                        true, new MockMultipartFile("EMPLOYEE", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))),
                        "4000", "boss", "+3595858554"));
        verify(emailService).sendRegistrationEmail((String) any(), (String) any(), (UserRegisterDto) any());
        verify(userRepository).save((Employee) any());
    }
}

