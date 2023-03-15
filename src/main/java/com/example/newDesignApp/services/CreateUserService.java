package com.example.newDesignApp.services;

import com.example.newDesignApp.dto.UserRegisterDto;
import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.enums.Role;
import com.example.newDesignApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class CreateUserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EmailService emailService;


    public void createUser(UserRegisterDto registerDto) throws MessagingException, IOException {
        Admin admin = new Admin();
        Employee employee = new Employee();
        if (registerDto.getRole().equals(Role.ADMIN)){
            admin.setEmail(registerDto.getEmail());
            admin.setPassword(registerDto.getPassword());
            admin.setFullName(registerDto.getFullName());
            admin.setUsername(registerDto.getUsername());
            admin.setRole(registerDto.getRole());
            admin.setEnabled(registerDto.isEnabled());
            userRepo.save(admin);
        }else {
            employee.setEmail(registerDto.getEmail());
            employee.setPassword(registerDto.getPassword());
            employee.setFullName(registerDto.getFullName());
            employee.setUsername(registerDto.getUsername());
            employee.setRole(registerDto.getRole());
            employee.setEnabled(registerDto.isEnabled());
            userRepo.save(employee);
        }
        String to = registerDto.getEmail();
        String subject = "Потвърждение за създаден акаунт!";


        emailService.sendRegistrationEmail(to, subject, registerDto);
    }
}
