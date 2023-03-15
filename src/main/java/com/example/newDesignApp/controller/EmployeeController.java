package com.example.newDesignApp.controller;

import com.example.newDesignApp.dto.StatisticsDto;
import com.example.newDesignApp.dto.UserRegisterDto;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.enums.Role;
import com.example.newDesignApp.repository.DailyProtocolRepository;
import com.example.newDesignApp.repository.EmployeeRepository;
import com.example.newDesignApp.repository.UserRepository;
import com.example.newDesignApp.services.AuthenticationService;
import com.example.newDesignApp.services.ImageProcessService;
import com.example.newDesignApp.services.Statistics;
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
@RequestMapping("/admin/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private Statistics statistics;

    @Autowired
    private ImageProcessService imageProcessService;

    private void getLoggedInInfo(Model model) {
        model.addAttribute("account", userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
    }

    @GetMapping("/list")
    public String listAllEmployees(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allEmployees", employeeRepository.findAll());
        return "admin/employees/listEmployees";
    }

    @GetMapping("/add")
    public String addEmployee(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("registerDto", new UserRegisterDto());
        return "admin/employees/addEmployee";
    }


    @PostMapping("/submit")
    public String addUser(@ModelAttribute("registerDto")
                          @Valid UserRegisterDto registerDto,
                          BindingResult bindingResult, Model model) throws IOException, MessagingException {
        getLoggedInInfo(model);
        if (userService.ifUserExists(registerDto.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Потребителското Име вече е заето. Моля изберете друго.");
            return ("admin/employees/addEmployee");
        }
        if (bindingResult.hasErrors()) {
            return ("admin/employees/addEmployee");
        } else {
            registerDto.setRole(Role.EMPLOYEE);
            userService.createUser(registerDto);

            return "redirect:/admin/employees/list";
        }
    }

    @GetMapping("/profile/details/{employeeId}")
    public String getProfileDetails(@PathVariable(name = "employeeId") Long employeeId, Model model) {
        model.addAttribute("account", userRepository.getUserByUsername(authenticationService.getWhoIsLoggedInUserName()));
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            model.addAttribute("employee", employee);
            model.addAttribute("employeeProjectsList",dailyProtocolRepository.getDistinctProjectsListByEmployeeId(employeeId));
            model.addAttribute("employeeCurrentWeekProtocolsList",statistics.getSpecificEmployeeWeekProtocols(employeeId));
            model.addAttribute("currentWeekNumber",statistics.getCurrentWeekNumber());

            byte[] imageData = employee.getProfilePicture();
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

        return "admin/employees/employeeDetails";
    }

    @GetMapping("/edit/{employeeId}")
    public String editEmployee(@PathVariable(name = "employeeId") Long employeeId,
                               Model model) {
        getLoggedInInfo(model);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            model.addAttribute("employee", employee);

            byte[] imageData = employee.getProfilePicture();
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

        return "admin/employees/editEmployeeDetails";
    }

    @PostMapping("/update/{employeeId}")
    public ModelAndView updateEmployee(
            @RequestParam(value = "password") String password,
            @RequestParam(value = "profilePictureUpdate") MultipartFile profilePicture,
            @PathVariable(name = "employeeId") Long employeeId,
            @ModelAttribute("employee") @Valid Employee updatedEmployee,
            BindingResult bindingResult)
            throws MessagingException, IOException {

        if (userService.updateIfUserExists(updatedEmployee)) {
            bindingResult.rejectValue("username", "error.username", "Потребителското Име вече е заето. Моля изберете друго.");
            return new ModelAndView("admin/employees/editEmployeeDetails");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/employees/editEmployeeDetails");
        }

        userService.updateUser(updatedEmployee, password,imageProcessService.convertMultipartToByteArray(profilePicture));
        return new ModelAndView("redirect:/admin/employees/list");
    }

    @GetMapping("delete/{userId}")
    public ModelAndView deleteEmployee(@PathVariable(name = "userId") Long userId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(userId);
        if (optionalEmployee.isPresent()) {
            employeeRepository.delete(optionalEmployee.get());
            return new ModelAndView("redirect:/admin/employees/list");
        } else {
            return new ModelAndView("redirect:/admin/employees/list");
        }
    }

    @GetMapping("/stats")
    public String showStatistics(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("statisticsDto", new StatisticsDto());
        return "admin/employees/statistics";
    }


    @PostMapping("/statsResult")
    public ModelAndView submitStatistics(
            @ModelAttribute("statisticsDto")
            @Valid StatisticsDto statisticsDto,
            BindingResult bindingResult,
            Model model
    ) throws MessagingException, IOException {
        getLoggedInInfo(model);
        String username = null;
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/admin/employees/statistics");
        } else if (statisticsDto.getEmployeeName() != null) { //username is used
            if (statistics.ifEmployeeExists(statisticsDto.getEmployeeName())) { // if user with that username exists
                model.addAttribute("status", username = "present"); //passing status to view
                model.addAttribute("allDailyProtocols", statistics.getEmployeeProtocols(statisticsDto.getEmployeeName()));
                model.addAttribute("fullName", statistics.getEmployeeFullName(statisticsDto.getEmployeeName()));
                model.addAttribute("sumTotalHoursWorked", statistics.sumTotalHoursEmployee(statisticsDto.getEmployeeName()));
                return new ModelAndView("admin/employees/listStatistics");
            } else {
                bindingResult.rejectValue("employeeName", "error.employeeName", "Потребителското Име не е намерено. Моля изберете друго.");
                return new ModelAndView("/admin/employees/statistics");
            }
        } else {
            if (statisticsDto.getWeekNumber() != null) {
                if ((statistics.validateWeekNumber(statisticsDto.getWeekNumber()))) {

                    int weekNumber = Integer.parseInt(statisticsDto.getWeekNumber());
                    model.addAttribute("DailyProtocolsByWeekNumber", statistics.collectProtocolsFromSpecificWeek(statistics.getMondayDate(weekNumber), statistics.getSundayDate(weekNumber)));
                    model.addAttribute("weekNumber", statisticsDto.getWeekNumber());
                    model.addAttribute("sumTotalHours", statistics.sumTotalHours(statistics.getMondayDate(weekNumber), statistics.getSundayDate(weekNumber)));
                } else {
                    bindingResult.rejectValue("weekNumber", "error.weekNumber", "Максеималният брой седмици е " + statistics.getWeekCount());
                    return new ModelAndView("/admin/employees/statistics");
                }
            }
        }
        return new ModelAndView("admin/employees/listStatistics");
    }


}
