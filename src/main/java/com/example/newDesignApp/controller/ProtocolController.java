package com.example.newDesignApp.controller;


import com.example.newDesignApp.entity.DailyProtocol;
import com.example.newDesignApp.repository.*;
import com.example.newDesignApp.services.AuthenticationService;
import com.example.newDesignApp.services.ClientProjectService;
import com.example.newDesignApp.services.DateConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/protocol")
public class ProtocolController {
    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DateConversionService dateConversionService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientProjectService clientProjectService;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private void getLoggedInInfo(Model model) {
        model.addAttribute("account", userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
    }

    @GetMapping("/list")
    public String listDailyProtocol(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("dateNow", dateConversionService.getCurrentDateFormatted());
        //model.addAttribute("allDailyProtocols", dailyProtocolRepository.getProtocolByProtocolDate(dateConversionService.getCurrentDateFormatted()));
        model.addAttribute("employeeDailyProtocols",
                dailyProtocolRepository.findBySpecificEmployeeIdAndProtocolDate(userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()).getId(),dateConversionService.getCurrentDateFormatted()));
        return "protocol/listProtocols";
    }

    @GetMapping("/SelectedClientProject.js")
    public ResponseEntity<Resource> getSelectedClientProjectJs() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/own/scripts/SelectedClientProject.js");
        MediaType mediaType = MediaType.parseMediaType("application/javascript");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(resource);
    }

    @GetMapping("/add")
    public String addProtocol(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("projects",projectRepository.findAll());
        model.addAttribute("dateNow", dateConversionService.getCurrentDateFormatted());
        model.addAttribute("dailyProtocol", new DailyProtocol());
        return "protocol/addProtocol";
    }

    @PostMapping("/submit")
    public ModelAndView submitProtocol(@Valid DailyProtocol dailyProtocol, BindingResult bindingResult, Model model) {
        model.addAttribute("allClients", clientRepository.findAll());
        System.out.println(dateConversionService.getCurrentDateFormatted());
        getLoggedInInfo(model);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("redirect:protocol/add");
        } else {
            dailyProtocol.setProtocolDate(dateConversionService.getCurrentDateFormatted());

            dailyProtocol.setEmployee(employeeRepository.getEmployeeByUsername(authenticationService.getWhoIsLoggedInUserName()));
            dailyProtocolRepository.save(dailyProtocol);

            return new ModelAndView("redirect:/protocol/list");
        }
    }

    @GetMapping("/edit/{dailyProtocolId}")
    public String editClient(@PathVariable(name = "dailyProtocolId") Long dailyProtocolId, Model model) {
        getLoggedInInfo(model);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("dateNow", dateConversionService.getCurrentDateFormatted());
        Optional<DailyProtocol> optionalDailyProtocol = dailyProtocolRepository.findById(dailyProtocolId);

        if (optionalDailyProtocol.isPresent()) {
            model.addAttribute("dailyProtocol", optionalDailyProtocol.get());
        } else {
            model.addAttribute("dailyProtocol", "Error!");
            model.addAttribute("errorMessage", "Protocol with id " + dailyProtocolId + " does not exist.");
        }
        return "protocol/editProtocol";
    }

    @PostMapping("/update")
    public ModelAndView updateClient(@Valid DailyProtocol dailyProtocol, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("protocol/edit/{protocolId}");
        }
        dailyProtocolRepository.save(dailyProtocol);
        return new ModelAndView("redirect:/protocol/list");
    }

    @GetMapping("delete/{dailyProtocolId}")
    public ModelAndView deleteClient(@PathVariable(name = "dailyProtocolId") Long dailyProtocolId) {
        Optional<DailyProtocol> optionalDailyProtocol = dailyProtocolRepository.findById(dailyProtocolId);
        if (optionalDailyProtocol.isPresent()) {
            dailyProtocolRepository.delete(optionalDailyProtocol.get());
        }
        return new ModelAndView("redirect:/protocol/list");
    }
}
