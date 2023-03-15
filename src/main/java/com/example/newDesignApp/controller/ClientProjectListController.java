package com.example.newDesignApp.controller;

import com.example.newDesignApp.dto.ClientDataDto;
import com.example.newDesignApp.entity.Client;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.Project;
import com.example.newDesignApp.repository.ClientRepository;
import com.example.newDesignApp.repository.DailyProtocolRepository;
import com.example.newDesignApp.services.ClientProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClientProjectListController {

    @Autowired
    private ClientProjectService clientProjectService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;

    @GetMapping("protocol/projectsList/{clientId}")
    public List<Project> getProjectList(@PathVariable(name = "clientId") Long clientId) {
        Client client = clientProjectService.getClientById(clientId);
        return client.getProjectsList();
    }


    @GetMapping("client/occupiedEmployeesList/{clientId}")
    public List<Employee> getClientOccupiedEmployeesList(@PathVariable(name = "clientId") Long clientId) {
        return dailyProtocolRepository.getListOfDistinctEmployeesByClient(clientId);
    }

    @GetMapping("client/allClientsList")
    public List<Client> getAllClients() {
        return (List<Client>) clientRepository.findAll();
    }

    @GetMapping("client/allClientsData")
    public List<Map<String, Object>> getAllClientsData() {
        List<Client> clients = (List<Client>) clientRepository.findAll();
        List<Map<String, Object>> clientDataList = new ArrayList<>();
        for (Client client : clients) {
            Map<String, Object> clientData = new HashMap<>();
            clientData.put("clientName", client.getClientName());
            clientData.put("numProjects", client.getProjectsList().size());
            clientDataList.add(clientData);
        }
        return clientDataList;
    }
}
