package com.example.newDesignApp.services;

import com.example.newDesignApp.entity.User;
import com.example.newDesignApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public String getWhoIsLoggedInUserName() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }
    public String getWhoIsLoggedInFullName() {
        User user=userRepository.getUserByUsername(getWhoIsLoggedInUserName());
        return user.getFullName();
    }

    public byte[] getUserImage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        return user.getProfilePicture();
    }

    public String getLoggedInRole() {
        String role ="EMPLOYEE";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) {
                role= authority.getAuthority();
            }
        }
        return role;
    }
}
