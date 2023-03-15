package com.example.newDesignApp.services;

import com.example.newDesignApp.entity.Client;
import com.example.newDesignApp.entity.DailyProtocol;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.Project;
import com.example.newDesignApp.repository.ClientRepository;
import com.example.newDesignApp.repository.DailyProtocolRepository;
import com.example.newDesignApp.repository.EmployeeRepository;
import com.example.newDesignApp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientProjectService {

    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private DateConversionService dateConversionService;

    public void createClientAndProject(String clientName, String projectName, String expirationDate) {
        Client client = new Client();
        client.setClientName(clientName);
        clientRepository.save(client);
        createProject(projectName, expirationDate, clientRepository.getClientByClientName(clientName));
    }

    private void createProject(String projectName, String expirationDate, Client client) {
        Project project = new Project();
        project.setProjectName(projectName);
        project.setExpirationDate(expirationDate);
        project.setClient(client);
        projectRepository.save(project);
    }

    public void createProject(Project project, String expirationDate) {
        project.setExpirationDate(dateConversionService.getExpirationDateFormatted(expirationDate));
        projectRepository.save(project);
    }

    public boolean ifClientExists(String wannaBeClientName){
        boolean exists = false;
        List<Client> clients = (List<Client>) clientRepository.findAll();
        for (Client client: clients) {
            if (client.getClientName().equals(wannaBeClientName)){
                exists=true;
            }
        }
        return exists;
    }


    public boolean ifProjectExists(Project wannaBeProject) {
        boolean exists = false;
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            if (project.getProjectName().equals(wannaBeProject.getProjectName())) //check if project name already exists
            {
                exists = true;
            }
        }
        return exists;
    }

    public Client getClientById(Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        Client client = new Client();
        if (optionalClient.isPresent()) {
            client = optionalClient.get();
        }
        return client;
    }







//    public boolean dailyWorkHoursLimitReached(Long employeeId, int employeeRequestedHours) {
//        boolean workHoursLimitReached = false;
//        int currentDayWorkedHours=0;
//        List<DailyProtocol> employeeTodayProtocols = getEmployeeTodayProtocols(getEmployeeById(employeeId).getDailyProtocol());
//        if (employeeTodayProtocols.size() > 0) {
//            for (DailyProtocol protocol: employeeTodayProtocols) {
//                currentDayWorkedHours+= protocol.getTimeWorked();
//            }
//            if ((currentDayWorkedHours+employeeRequestedHours)>8){
//                workHoursLimitReached=true;
//            }
//        }
//        return workHoursLimitReached;
//    }
//
//    private Employee getEmployeeById(Long employeeId) {
//        Employee employee = new Employee();
//        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
//        if (optionalEmployee.isPresent()) {
//            employee = optionalEmployee.get();
//        }
//        return employee;
//    }
//
//    private List<DailyProtocol> getEmployeeTodayProtocols(List<DailyProtocol> employeeAllDailyProtocols) {
//        List<DailyProtocol> employeeTodayProtocols = new ArrayList<>();
//        for (DailyProtocol dailyProtocol : employeeAllDailyProtocols) {
//            if (dailyProtocol.getProtocolDate().equals(dateConversionService.getCurrentDateFormatted())) {
//                employeeTodayProtocols.add(dailyProtocol);
//            }
//        }
//        return employeeTodayProtocols;
//    }
}

