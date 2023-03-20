package com.example.newDesignApp.services;

import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.enums.Role;
import com.example.newDesignApp.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AuthenticationService.class})
@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        // Initialize mock objects
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWhoIsLoggedInUserName() {
        // Create an instance of the Authentication class to test
        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }


        };
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "testpassword"));
        String result = authenticationService.getWhoIsLoggedInUserName();

        Assert.assertEquals("testuser", result);
    }


    @Test
    public void testGetWhoIsLoggedInFullName() {

        Authentication authentication = new Authentication() {
            @Override
            public boolean equals(Object another) {
                return false;
            }

            @Override
            public String toString() {
                return null;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "testpassword"));

        Employee mockEmployee = new Employee();
        mockEmployee.setFullName("Test User");
        when(userRepository.getUserByUsername("testuser")).thenReturn(mockEmployee);

        String result = authenticationService.getWhoIsLoggedInFullName();

        Assert.assertEquals("Test User", result);
    }

    @Test
    public void testGetUserImage1() {
        Employee employee = new Employee();
        byte[] mockImage = new byte[]{0x12, 0x34, 0x56};
        employee.setProfilePicture(mockImage);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "testpassword"));


        Employee mockEmployee = new Employee();
        mockEmployee.setProfilePicture(mockImage);
        when(userRepository.getUserByUsername("testuser")).thenReturn(mockEmployee);

        byte[] result = employee.getProfilePicture();

        assertArrayEquals(mockImage, result);
    }

    @Test
    public void testGetUserImage2() {
        byte[] mockImage = new byte[]{0x12, 0x34, 0x56};
        // Create a test user with a profile picture
        Admin admin = new Admin();
        admin.setUsername("adminUser");
        admin.setPassword("1234");
        admin.setEmail("testuser@example.com");
        admin.setRole(Role.ADMIN);
        admin.setFullName("Test User");
        admin.setProfilePicture(mockImage);
        // Set up the mock UserRepository to return the test user
        Mockito.when(userRepository.getUserByUsername("adminUser")).thenReturn(admin);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("adminUser", "1234"));
        // Call the getUserImage method and check the result
        assertArrayEquals(authenticationService.getUserImage(), admin.getProfilePicture());
    }

    @Test
    public void testGetLoggedInRole() {
        Employee employee = new Employee();

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("EMPLOYEE"));
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken("testuser", "testpassword", authorities);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        String result = authenticationService.getLoggedInRole();

        Assert.assertEquals("EMPLOYEE", result);
    }
}

