package com.example.newDesignApp.controller;

import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.User;
import com.example.newDesignApp.repository.AdminRepository;
import com.example.newDesignApp.repository.EmployeeRepository;
import com.example.newDesignApp.repository.UserRepository;
import com.example.newDesignApp.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        User user=userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName());
        model.addAttribute("account", user);
        if (user.getRole().label.equals("Админ")){
            model.addAttribute("status","ADMIN");
        }
        return "/account/profile";
    }

    @GetMapping("/profile/image")
    public ResponseEntity<byte[]> getCurrentlyLoggedUserImage() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] image = authenticationService.getUserImage();
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @GetMapping("/list/profilePicture/{employeeId}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        Employee employee = new Employee();
        if (optionalEmployee.isPresent()) {
            employee = optionalEmployee.get();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(employee.getProfilePicture());
    }
    @GetMapping("/list/adminProfilePicture/{adminId}")
    public ResponseEntity<byte[]> getAdminProfilePicture(@PathVariable Long adminId) {
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        Admin admin = new Admin();
        if (optionalAdmin.isPresent()) {
            admin = optionalAdmin.get();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(admin.getProfilePicture());
    }

    @PostMapping("/updateProfilePicture")
    public String updateProfilePicture(@PathVariable Long employeeId){
        return "admin/employeeDetails/{employeeId}";
    }

    @GetMapping("/test")
    public String getCircleProgress(Model model) {
        model.addAttribute("title", "Circle Progress Bar");
        return "/test";
    }
}

