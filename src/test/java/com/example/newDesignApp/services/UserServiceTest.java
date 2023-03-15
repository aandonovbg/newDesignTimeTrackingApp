package com.example.newDesignApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.newDesignApp.dto.UserRegisterDto;
import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.DailyProtocol;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.User;
import com.example.newDesignApp.enums.Role;
import com.example.newDesignApp.repository.AdminRepository;
import com.example.newDesignApp.repository.EmployeeRepository;
import com.example.newDesignApp.repository.UserRepository;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private ResourceLoader resourceLoader;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void testIfUserExistsFalse() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        assertFalse(userService.ifUserExists("xxx"));
        verify(userRepository).findAll();
    }

    @Test
    void testIfUserExistsTrue() {
        Employee employee = new Employee();
        employee.setUsername("xxx");
        ArrayList<User> userList = new ArrayList<>();
        userList.add(employee);
        when(userRepository.findAll()).thenReturn(userList);
        assertTrue(userService.ifUserExists("xxx"));
    }


    @Test
    void testIfUserExists() {
        Admin admin = mock(Admin.class);
        when(admin.getUsername()).thenReturn("anton");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(admin);
        when(userRepository.findAll()).thenReturn(userList);
        assertTrue(userService.ifUserExists("anton"));
        verify(userRepository).findAll();
        verify(admin).getUsername();
    }

    @Test
    void testUpdateIfUserExists() throws UnsupportedEncodingException {
        Employee employee = new Employee();
        employee.setAddress("Тцар Осв");
        employee.setCity("Тарго");
        employee.setDailyProtocol(new ArrayList<>());
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Антон Андонож");
        employee.setGSM("+3598584654");
        employee.setId(1L);
        employee.setJobTitle("boss");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("5000");
        employee.setUsername("aandonov");
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);
        when(userRepository.getAllUsersExcludingUsername((String) any())).thenReturn(new ArrayList<>());
        assertFalse(userService.updateIfUserExists(new Admin()));
        verify(employeeRepository).findById((Long) any());
        verify(userRepository).getAllUsersExcludingUsername((String) any());
    }

    @Test
    void testUpdateIfUserExists2() throws UnsupportedEncodingException {
        Employee employee = mock(Employee.class);
        when(employee.getUsername()).thenReturn("aandonov");
        doNothing().when(employee).setDailyProtocol((List<DailyProtocol>) any());
        doNothing().when(employee).setAddress((String) any());
        doNothing().when(employee).setCity((String) any());
        doNothing().when(employee).setEmail((String) any());
        doNothing().when(employee).setEnabled(anyBoolean());
        doNothing().when(employee).setFullName((String) any());
        doNothing().when(employee).setGSM((String) any());
        doNothing().when(employee).setId((Long) any());
        doNothing().when(employee).setJobTitle((String) any());
        doNothing().when(employee).setPassword((String) any());
        doNothing().when(employee).setProfilePicture((byte[]) any());
        doNothing().when(employee).setRole((Role) any());
        doNothing().when(employee).setSalary((String) any());
        doNothing().when(employee).setUsername((String) any());
        employee.setAddress("Тцар Осв");
        employee.setCity("Тарго");
        employee.setDailyProtocol(new ArrayList<>());
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Антон Андонож");
        employee.setGSM("+3598584654");
        employee.setId(1L);
        employee.setJobTitle("boss");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("5000");
        employee.setUsername("aandonov");
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);
        when(userRepository.getAllUsersExcludingUsername((String) any())).thenReturn(new ArrayList<>());
        assertFalse(userService.updateIfUserExists(new Admin()));
        verify(employeeRepository).findById((Long) any());
        verify(employee).getUsername();
        verify(employee).setDailyProtocol((List<DailyProtocol>) any());
        verify(employee).setAddress((String) any());
        verify(employee).setCity((String) any());
        verify(employee).setEmail((String) any());
        verify(employee).setEnabled(anyBoolean());
        verify(employee).setFullName((String) any());
        verify(employee).setGSM((String) any());
        verify(employee).setId((Long) any());
        verify(employee).setJobTitle((String) any());
        verify(employee).setPassword((String) any());
        verify(employee).setProfilePicture((byte[]) any());
        verify(employee).setRole((Role) any());
        verify(employee).setSalary((String) any());
        verify(employee).setUsername((String) any());
        verify(userRepository).getAllUsersExcludingUsername((String) any());
    }


    @Test
    void testUpdateIfUserExistsFalse() throws UnsupportedEncodingException {
        Employee employee = new Employee();
        employee.setAddress("Тцар Осв");
        employee.setCity("Тарго");
        employee.setDailyProtocol(new ArrayList<>());
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Антон Андонож");
        employee.setGSM("+3598584654");
        employee.setId(1L);
        employee.setJobTitle("boss");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("5000");
        employee.setUsername("aandonov");
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);
        when(userRepository.getAllUsersExcludingUsername((String) any())).thenReturn(new ArrayList<>());
        assertFalse(userService.updateIfUserExists(new Admin()));
        verify(employeeRepository).findById((Long) any());
        verify(userRepository).getAllUsersExcludingUsername((String) any());
    }

    @Test
    void testUpdateIfUserExistsThrows() throws UnsupportedEncodingException {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCity("Oxford");
        employee.setDailyProtocol(new ArrayList<>());
        employee.setEmail("jane.doe@example.org");
        employee.setEnabled(true);
        employee.setFullName("Dr Jane Doe");
        employee.setGSM("GSM");
        employee.setId(1L);
        employee.setJobTitle("Dr");
        employee.setPassword("iloveyou");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("Salary");
        employee.setUsername("janedoe");
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);
        when(userRepository.getAllUsersExcludingUsername((String) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.updateIfUserExists(new Admin()));
        verify(employeeRepository).findById((Long) any());
        verify(userRepository).getAllUsersExcludingUsername((String) any());
    }

    @Test
    void testCreateUser() throws IOException, MessagingException {
        doNothing().when(emailService).sendRegistrationEmail((String) any(), (String) any(), (UserRegisterDto) any());

        Employee employee = new Employee();
        employee.setAddress("Тцар Осв");
        employee.setCity("Тарго");
        employee.setDailyProtocol(new ArrayList<>());
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Антон Андонов");
        employee.setGSM("+3598584654");
        employee.setId(1L);
        employee.setJobTitle("boss");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("5000");
        employee.setUsername("aandonov");
        when(userRepository.save((Employee) any())).thenReturn(employee);
        userService
                .createUser(new UserRegisterDto("aandonov", "Антон Андонов", "xxx@xxx.bg", "1234", Role.EMPLOYEE,
                        true, new MockMultipartFile("EMPLOYEE", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))),
                        "5000", "boss", "+3598584654"));
        verify(emailService).sendRegistrationEmail((String) any(), (String) any(), (UserRegisterDto) any());
        verify(userRepository).save((Employee) any());
    }

    @Test
    void testCreateUserThrow() throws IOException, MessagingException {
        doNothing().when(emailService).sendRegistrationEmail((String) any(), (String) any(), (UserRegisterDto) any());
        when(userRepository.save((Employee) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class,
                () -> userService.createUser(
                        new UserRegisterDto("aandonov", "Антон Андонов", "xxx@xxx.bg", "1234", Role.EMPLOYEE,
                                true, new MockMultipartFile("EMPLOYEE", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))),
                                "5000", "boss", "+3598584654")));
        verify(userRepository).save((Employee) any());
    }

    @Test
    void testUpdateUser() throws IOException, MessagingException {
        doNothing().when(emailService).sendUpdateEmail((String) any(), (String) any(), (User) any(), (String) any());

        Admin admin = new Admin();
        admin.setAddress("Тцар Осв");
        admin.setCity("Тарго");
        admin.setEmail("xxx@xxx.bg");
        admin.setEnabled(true);
        admin.setFullName("Антон Андонов");
        admin.setGSM("+35984683544");
        admin.setId(1L);
        admin.setJobTitle("boss");
        admin.setPassword("1234");
        admin.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        admin.setRole(Role.EMPLOYEE);
        admin.setSalary("5000");
        admin.setUsername("aandonov");
        when(userRepository.save((Admin) any())).thenReturn(admin);
        Admin admin1 = new Admin();
        userService.updateUser(admin1, "1234", "AXAXAXAX".getBytes("UTF-8"));
        verify(emailService).sendUpdateEmail((String) any(), (String) any(), (User) any(), (String) any());
        verify(userRepository).save((Admin) any());
        assertEquals(8, admin1.getProfilePicture().length);
    }

    @Test
    void testUpdateUserThrow() throws IOException, MessagingException {
        doNothing().when(emailService).sendUpdateEmail((String) any(), (String) any(), (User) any(), (String) any());
        when(userRepository.save((Admin) any())).thenThrow(new RuntimeException());
        Admin user = new Admin();
        assertThrows(RuntimeException.class,
                () -> userService.updateUser(user, "iloveyou", "AXAXAXAX".getBytes("UTF-8")));
        verify(userRepository).save((Admin) any());
    }
}

