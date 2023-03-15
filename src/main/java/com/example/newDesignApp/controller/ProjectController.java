package com.example.newDesignApp.controller;

import com.example.newDesignApp.dto.ClientProjectDto;
import com.example.newDesignApp.entity.Client;
import com.example.newDesignApp.entity.Project;
import com.example.newDesignApp.repository.ClientRepository;
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
import java.util.Optional;

@Controller
@RequestMapping("/admin/projects")
public class ProjectController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AuthenticationService authenticationService;


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientProjectService clientProjectService;


    private void getLoggedInInfo(Model model) {
        model.addAttribute("account",userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
    }

    @GetMapping("/list")
    public String listAllClients(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allProjects", projectRepository.findAll());

        return "admin/projects/listProjects";
    }

    @GetMapping("/add")
    public String addClient(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allClients", clientRepository.findAll());
        model.addAttribute("project", new Project());
        return "admin/projects/addProject";
    }

    @PostMapping("/submit")
    public ModelAndView submit(
            @RequestParam("expirationDate") String expirationDate,
            @Valid Project project,
            BindingResult bindingResult) {
        if (clientProjectService.ifProjectExists(project)) {
            bindingResult.rejectValue("projectName", "error.projectName", "Вече има такъв Проект. Моля изберете друго име.");
            return new ModelAndView("admin/clients/addClient");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/clients/addClient");
        } else {

            clientProjectService.createProject(project,expirationDate);
            return new ModelAndView("redirect:/admin/clients/list");
        }
    }

    @GetMapping("/edit/{projectId}")
    public String editProject(@PathVariable(name = "projectId") Long projectId, Model model) {
        getLoggedInInfo(model);
        Optional<Project>optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            model.addAttribute("projectCurrentDate",project.getExpirationDate());
            model.addAttribute("project", project);
        } else {
            model.addAttribute("employee", "Error!");
            model.addAttribute("errorMessage", "Project with id " + projectId + " does not exist.");
        }
        return "admin/projects/editProject";
    }

    @PostMapping("/update")
    public ModelAndView updateProject(@Valid Project project,
                                      @RequestParam("expirationDate") String expirationDate,
                                      @RequestParam("id") Long projectId,
                                      BindingResult bindingResult,Model model) {
        getLoggedInInfo(model);
        if (clientProjectService.ifProjectExists(project)) {
            bindingResult.rejectValue("projectName", "error.projectName", "Вече има такъв Проект. Моля изберете друго име.");
            return new ModelAndView ("admin/projects/editProject");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/projects/editProject");
        }
        project.setClient(projectRepository.getClientByProjectId(projectId));
        clientProjectService.createProject(project,expirationDate);
        return new ModelAndView("redirect:/admin/projects/list");
    }

    @GetMapping("delete/{projectId}")
    public ModelAndView deleteProject(@PathVariable(name = "projectId") Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            projectRepository.delete(optionalProject.get());
        }
        return new ModelAndView("redirect:/admin/projects/list");
    }
}
