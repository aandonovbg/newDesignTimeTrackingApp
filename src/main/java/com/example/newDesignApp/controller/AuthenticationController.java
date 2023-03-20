package com.example.newDesignApp.controller;

import com.example.newDesignApp.repository.ClientRepository;
import com.example.newDesignApp.repository.DailyProtocolRepository;
import com.example.newDesignApp.repository.UserRepository;
import com.example.newDesignApp.services.AuthenticationService;
import com.example.newDesignApp.services.DateConversionService;
import com.example.newDesignApp.services.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DateConversionService dateConversionService;
    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;
    @Autowired
    private Statistics statistics;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "startup/login";
    }

    @GetMapping("/logout")
    public String logoutPage() {

        return "redirect:/login";
    }

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("account",userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
        model.addAttribute("whoIsLoggedIn", authenticationService.getWhoIsLoggedInUserName());
        model.addAttribute("role", authenticationService.getLoggedInRole());
        if (authenticationService.getLoggedInRole().equals("EMPLOYEE")) {
            model.addAttribute("allClients", clientRepository.findAll());
            model.addAttribute("dateNow", dateConversionService.getCurrentDateFormatted());
            model.addAttribute("employeeDailyProtocols",
                    dailyProtocolRepository.findBySpecificEmployeeIdAndProtocolDate(userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()).getId(),dateConversionService.getCurrentDateFormatted()));

            return "/protocol/listProtocols";
        }

        model.addAttribute("whoIsLoggedInFullName",authenticationService.getWhoIsLoggedInFullName());
        model.addAttribute("whoIsLoggedInImage",authenticationService.getUserImage());
        model.addAttribute("employeeCount",statistics.getEmployeesCount());
        model.addAttribute("clientCount", statistics.getClientCount());
        model.addAttribute("projectCount",statistics.getProjectsCount());
        model.addAttribute("allClientsList",statistics.getAllClientsList());
    return "/startup/index";
    }

    @PostMapping("/authenticate")
    public String authenticate(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpServletRequest request
    ) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            Authentication result = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(result);
            return "/startup/index";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}
