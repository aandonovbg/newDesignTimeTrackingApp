package com.example.newDesignApp.controller;

import com.example.newDesignApp.dto.ClientProjectDto;
import com.example.newDesignApp.entity.Client;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.Project;
import com.example.newDesignApp.repository.ClientRepository;
import com.example.newDesignApp.repository.DailyProtocolRepository;
import com.example.newDesignApp.repository.ProjectRepository;
import com.example.newDesignApp.repository.UserRepository;
import com.example.newDesignApp.services.AuthenticationService;
import com.example.newDesignApp.services.ClientProjectService;
import com.example.newDesignApp.services.DateConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Base64;
import java.util.Optional;

@Controller
@RequestMapping("/admin/clients")
public class ClientController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DateConversionService dateConversionService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientProjectService clientProjectService;

    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;

    private void getLoggedInInfo(Model model) {
        model.addAttribute("account",userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
    }

    @GetMapping("/list")
    public String listAllClients(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allClients", clientRepository.findAll());

        return "admin/clients/listClients";
    }

    @GetMapping("/add")
    public String addClient(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("clientProjectDto", new ClientProjectDto());
        return "admin/clients/addClient";
    }

    @PostMapping("/submit")
    public ModelAndView submit(
            @RequestParam("expirationDate") String expirationDate,
            @Valid ClientProjectDto clientProjectDto,
            BindingResult bindingResult,Model model) {
        getLoggedInInfo(model);
        if (clientProjectService.ifClientExists(clientProjectDto.getClientName())) {
            bindingResult.rejectValue("clientName", "error.clientName", "Вече има такъв клиент. Отидете в Проекти и добавете Проекта към Клиента.");
            return new ModelAndView("admin/clients/addClient");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/clients/addClient");
        } else {
            clientProjectService.createClientAndProject(clientProjectDto.getClientName(),clientProjectDto.getProjectName(),
                                                        dateConversionService.getExpirationDateFormatted(expirationDate));
            return new ModelAndView("redirect:/admin/clients/list");
        }
    }

    @GetMapping("/details/{clientId}")
    public String getClientDetails(@PathVariable(name = "clientId") Long clientId, Model model) {
        model.addAttribute("account", userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            model.addAttribute("client", client);
            model.addAttribute("clientProjectsList",client.getProjectsList());
            model.addAttribute("occupiedEmployeesList",dailyProtocolRepository.getListOfDistinctEmployeesByClient(clientId));
        }

        return "admin/clients/clientDetails";
    }

    @GetMapping("/edit/{clientId}")
    public String editClient(@PathVariable(name = "clientId") Long clientId, Model model) {
        getLoggedInInfo(model);
        Optional<Client> optionalClient = clientRepository.findById(clientId);

        if (optionalClient.isPresent()) {
            model.addAttribute("client", optionalClient.get());
        } else {
            model.addAttribute("employee", "Error!");
            model.addAttribute("errorMessage", "Employee with id " + clientId + " does not exist.");
        }
        return "admin/clients/editClient";
    }

    @PostMapping("/update")
    public ModelAndView updateClient(@Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/clients/editClient/{clientId}");
        }
        clientRepository.save(client);
        return new ModelAndView("redirect:/admin/clients/list");
    }

    @GetMapping("delete/{clientId}")
    public ModelAndView deleteClient(@PathVariable(name = "clientId") Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isPresent()) {
            clientRepository.delete(optionalClient.get());
        }
        return new ModelAndView("redirect:/admin/clients/list");
    }
}
