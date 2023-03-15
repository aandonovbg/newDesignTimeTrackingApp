package com.example.newDesignApp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.newDesignApp.entity.Admin;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserDetailsServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplementationTest {
    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        when(userRepository.getUserByUsername((String) any())).thenReturn(new Admin());
        assertNull(userDetailsServiceImplementation.loadUserByUsername("xxx").getPassword());
        verify(userRepository).getUserByUsername((String) any());
    }

    @Test
    void testLoadUserByUsernameThrows() throws UsernameNotFoundException {
        when(userRepository.getUserByUsername((String) any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsServiceImplementation.loadUserByUsername("xxx"));
        verify(userRepository).getUserByUsername((String) any());
    }
}

