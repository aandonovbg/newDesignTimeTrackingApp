package com.example.newDesignApp.controller;

import com.example.newDesignApp.dto.UserRegisterDto;
import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.enums.Role;
import com.example.newDesignApp.repository.AdminRepository;
import com.example.newDesignApp.repository.UserRepository;
import com.example.newDesignApp.services.AuthenticationService;
import com.example.newDesignApp.services.ImageProcessService;
import com.example.newDesignApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Controller
@RequestMapping("admin/")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageProcessService imageProcessService;

    private void getLoggedInInfo(Model model) {
        model.addAttribute("account",userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
        model.addAttribute("role", authenticationService.getLoggedInRole());
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        getLoggedInInfo(model);
        return "admin/dashboard";
    }

    @GetMapping("/list")
    public String listAllAdmin(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allAdmins", adminRepository.findAll());
        return "admin/admins/listAdmins";
    }

    @GetMapping("/add")
    public String addAdmin(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("registerDto", new UserRegisterDto());
        return "admin/admins/addAdmin";
    }

    @PostMapping("/submit")
    public String addUser(@ModelAttribute("registerDto")
                          @Valid UserRegisterDto registerDto,
                          BindingResult bindingResult, Model model) throws IOException, MessagingException {
        getLoggedInInfo(model);
        if (userService.ifUserExists(registerDto.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Потребителското Име вече е заето. Моля изберете друго.");
            return ("admin/admins/addAdmin");
        }
        if (bindingResult.hasErrors()) {
            return ("admin/admins/addAdmin");
        } else {
            registerDto.setRole(Role.ADMIN);
            userService.createUser(registerDto);

            return "redirect:/admin/list";
        }
    }

    @GetMapping("/profile/details/{adminId}")
    public String getProfileDetails(@PathVariable(name = "adminId") Long adminId, Model model) {
        model.addAttribute("account", userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            model.addAttribute("admin", admin);
//            model.addAttribute("employeeProjectsList",dailyProtocolRepository.getDistinctProjectsListByEmployeeId(adminId));
//            model.addAttribute("employeeCurrentWeekProtocolsList",statistics.getSpecificEmployeeWeekProtocols(adminId));
//            model.addAttribute("currentWeekNumber",statistics.getCurrentWeekNumber());

            byte[] imageData = admin.getProfilePicture();
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            String imageFormat = "png"; // default
            if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
                imageFormat = "jpeg";
            } else if (imageData[0] == (byte) 0x47 && imageData[1] == (byte) 0x49 && imageData[2] == (byte) 0x46) {
                imageFormat = "gif";
            }
            model.addAttribute("profilePicture", base64Image);
            model.addAttribute("imageFormat", imageFormat);
        }

        return "admin/admins/adminDetails";
    }

    @GetMapping("/edit/{adminId}")
    public String editAdmin(@PathVariable(name = "adminId") Long adminId, Model model) {
        getLoggedInInfo(model);
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            model.addAttribute("admin", admin);

            byte[] imageData = admin.getProfilePicture();
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            String imageFormat = "png"; // default
            if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
                imageFormat = "jpeg";
            } else if (imageData[0] == (byte) 0x47 && imageData[1] == (byte) 0x49 && imageData[2] == (byte) 0x46) {
                imageFormat = "gif";
            }
            model.addAttribute("profilePicture", base64Image);
            model.addAttribute("imageFormat", imageFormat);
        }

        return "admin/admins/editAdminDetails";
    }

    @PostMapping("/update/{adminId}")
    public ModelAndView updateEmployee(
            @RequestParam(value = "password") String password,
            @RequestParam(value = "profilePictureUpdate") MultipartFile profilePicture,
            @PathVariable(name = "adminId") Long adminId,
            @ModelAttribute("admin") @Valid Admin updatedAdmin,
            BindingResult bindingResult)
            throws MessagingException, IOException {
        updatedAdmin.setRole(Role.ADMIN);
        if (userService.updateIfUserExists(updatedAdmin)) {
            bindingResult.rejectValue("username", "error.username", "Потребителското Име вече е заето. Моля изберете друго.");
            return new ModelAndView("admin/admins/editAdminDetails");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/admins/editAdminDetails");
        }

        userService.updateUser(updatedAdmin, password,imageProcessService.convertMultipartToByteArray(profilePicture));
        return new ModelAndView("redirect:/admin/list");
    }

    @GetMapping("delete/{adminId}")
    public ModelAndView deleteAdmin(@PathVariable(name = "adminId") Long adminId) {
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            adminRepository.delete(optionalAdmin.get());
            return new ModelAndView("redirect:/admin/admins/list");
        } else {
            return new ModelAndView("redirect:/admin/admins/list");
        }
    }
}
