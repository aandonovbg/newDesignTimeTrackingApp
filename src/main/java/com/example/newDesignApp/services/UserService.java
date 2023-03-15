package com.example.newDesignApp.services;

import com.example.newDesignApp.dto.UserRegisterDto;
import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.User;
import com.example.newDesignApp.enums.Role;
import com.example.newDesignApp.repository.AdminRepository;
import com.example.newDesignApp.repository.EmployeeRepository;
import com.example.newDesignApp.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private AdminRepository adminRepository;
    private final byte[] DEFAULT_PROFILE_PICTURE = loadDefaultProfilePicture();

    private byte[] loadDefaultProfilePicture() {
        try {
            Resource resource = new ClassPathResource("static/vendors/images/brandAW.png");
            byte[] data = IOUtils.toByteArray(resource.getInputStream());
            resource.getInputStream().read(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default profile picture", e);
        }
    }


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmployeeRepository employeeRepository;

    public boolean ifUserExists(String username){
        boolean exists = false;
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username)){
                exists = true;
            }
        }
        return exists;
    }
    public boolean updateIfUserExists(User user){
        boolean exists = false;
        if (user.getRole().equals(Role.EMPLOYEE)){
            Optional<Employee> optionalEmployee = employeeRepository.findById(user.getId());
            String employeeName="";
            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();
                employeeName=employee.getUsername();
            }
            List<User> users = userRepository.getAllUsersExcludingUsername(employeeName);
            for (User account : users) {
                if (account.getUsername().equals(user.getUsername())){
                    exists = true;
                    break;
                }
            }
        }else {
            Optional<Admin> optionalAdmin = adminRepository.findById(user.getId());
            String adminName="";
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                adminName=admin.getUsername();
            }
            List<User> users = userRepository.getAllUsersExcludingUsername(adminName);
            for (User account : users) {
                if (account.getUsername().equals(user.getUsername())){
                    exists = true;
                    break;
                }
            }
        }

        return exists;
    }


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
            admin.setSalary(registerDto.getSalary());
            MultipartFile multipartFile = registerDto.getImageFile(); // get the uploaded file

            if (multipartFile != null) {
                byte[] imageBytes = multipartFile.getBytes(); // convert the file to a byte array
                if (imageBytes.length > 0) {
                    admin.setProfilePicture(imageBytes); // set the byte array to the user entity
                }else {
                    admin.setProfilePicture(DEFAULT_PROFILE_PICTURE);
                }
            }
            userRepository.save(admin);
        }else {
            employee.setEmail(registerDto.getEmail());
            employee.setPassword(registerDto.getPassword());
            employee.setFullName(registerDto.getFullName());
            employee.setUsername(registerDto.getUsername());
            employee.setRole(registerDto.getRole());
            employee.setEnabled(registerDto.isEnabled());
            employee.setJobTitle(registerDto.getJobTitle());
            employee.setSalary(registerDto.getSalary());
            MultipartFile multipartFile = registerDto.getImageFile(); // get the uploaded file

            if (multipartFile != null) {
                byte[] imageBytes = multipartFile.getBytes(); // convert the file to a byte array
                if (imageBytes.length > 0) {
                    employee.setProfilePicture(imageBytes); // set the byte array to the user entity
                }else {
                    employee.setProfilePicture(DEFAULT_PROFILE_PICTURE);
                }
            }

            userRepository.save(employee);
        }
        String to = registerDto.getEmail();
        String subject = "Потвърждение за създаден акаунт!";

        emailService.sendRegistrationEmail(to, subject, registerDto);
    }


    public void updateUser(User user, String password, byte[] profilePicture) throws MessagingException, IOException {
        if (user.getRole().equals(Role.ADMIN)){
            if (profilePicture != null){
                if (profilePicture.length > 0){
                    user.setProfilePicture(profilePicture);
                }else {
                    Optional<Admin> optionalAdmin = adminRepository.findById(user.getId());
                    if (optionalAdmin.isPresent()) {
                        Admin admin = optionalAdmin.get();
                        user.setProfilePicture(admin.getProfilePicture());
                    }
                }
            }
        }else {

            if (profilePicture != null) {
                if (profilePicture.length > 0) {
                    user.setProfilePicture(profilePicture);
                } else {
                    Optional<Employee> optionalEmployee = employeeRepository.findById(user.getId());
                    if (optionalEmployee.isPresent()) {
                        Employee employee = optionalEmployee.get();
                        user.setProfilePicture(employee.getProfilePicture());
                    }
                }
            }
        }
        userRepository.save(user);
        String to = user.getEmail();
        String subject = "Предупреждение за актуализиране на акаунт!";

        emailService.sendUpdateEmail(to, subject,user, password);
    }
}
