package com.example.newDesignApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.newDesignApp.entity.Client;
import com.example.newDesignApp.entity.Project;
import com.example.newDesignApp.repository.ClientRepository;
import com.example.newDesignApp.repository.DailyProtocolRepository;
import com.example.newDesignApp.repository.EmployeeRepository;
import com.example.newDesignApp.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ClientProjectService.class})
@ExtendWith(SpringExtension.class)
class ClientProjectServiceTest {
    @Autowired
    private ClientProjectService clientProjectService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private DailyProtocolRepository dailyProtocolRepository;

    @MockBean
    private DateConversionService dateConversionService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @Test
    void testCreateClientAndProject() {
        Client client = new Client();
        client.setClientName("LudogorieSoft");
        client.setId(1L);
        client.setProjectsList(new ArrayList<>());

        Client client1 = new Client();
        client1.setClientName("LudogorieSoft");
        client1.setId(1L);
        client1.setProjectsList(new ArrayList<>());
        when(clientRepository.getClientByClientName((String) any())).thenReturn(client);
        when(clientRepository.save((Client) any())).thenReturn(client1);

        Client client2 = new Client();
        client2.setClientName("LudogorieSoft");
        client2.setId(1L);
        client2.setProjectsList(new ArrayList<>());

        Project project = new Project();
        project.setClient(client2);
        project.setExpirationDate("2023-15-03");
        project.setId(1L);
        project.setProjectName("Разработване на С-ма за следене на времето");
        when(projectRepository.save((Project) any())).thenReturn(project);
        clientProjectService.createClientAndProject("LudogorieSoft", "Разработване на С-ма за следене на времето", "2023-15-03");
        verify(clientRepository).getClientByClientName((String) any());
        verify(clientRepository).save((Client) any());
        verify(projectRepository).save((Project) any());
    }

    @Test
    void testCreateProject() {
        when(dateConversionService.getExpirationDateFormatted((String) any())).thenReturn("2023-15-03");

        Client client = new Client();
        client.setClientName("LudogorieSoft");
        client.setId(1L);
        client.setProjectsList(new ArrayList<>());

        Project project = new Project();
        project.setClient(client);
        project.setExpirationDate("2023-15-03");
        project.setId(1L);
        project.setProjectName("Разработване на С-ма за следене на времето");
        when(projectRepository.save((Project) any())).thenReturn(project);

        Client client1 = new Client();
        client1.setClientName("LudogorieSoft");
        client1.setId(1L);
        client1.setProjectsList(new ArrayList<>());

        Project project1 = new Project();
        project1.setClient(client1);
        project1.setExpirationDate("2023-15-03");
        project1.setId(1L);
        project1.setProjectName("Разработване на С-ма за следене на времето");
        clientProjectService.createProject(project1, "2023-15-03");
        verify(dateConversionService).getExpirationDateFormatted((String) any());
        verify(projectRepository).save((Project) any());
        assertEquals("2023-15-03", project1.getExpirationDate());
    }


    @Test
    public void testIfClientExists() {

        Client client=new Client();
        client.setClientName("Sisecam");
        client.setId(1L);
        client.setProjectsList(new ArrayList<>());
        List<Client> clients=new ArrayList<>();
        clients.add(client);
        when(clientRepository.findAll()).thenReturn(clients);

        boolean result1 = clientProjectService.ifClientExists("Sisecam");
        assertTrue(result1);

        boolean result2 = clientProjectService.ifClientExists("Anton");
        assertFalse(result2);
    }

    @Test
    void testIfProjectExists() {
        when(projectRepository.findAll()).thenReturn(new ArrayList<>());

        Client client = new Client();
        client.setClientName("Sisecam");
        client.setId(1L);
        client.setProjectsList(new ArrayList<>());

        Project project = new Project();
        project.setClient(client);
        project.setExpirationDate("2023-15-03");
        project.setId(1L);
        project.setProjectName("Разработване на С-ма за следене на времето");
        assertFalse(clientProjectService.ifProjectExists(project));
        verify(projectRepository).findAll();
    }

    @Test
    void testIfProjectExists2() {
        Client client = new Client();
        client.setClientName("LudogorieSoft");
        client.setId(1L);
        client.setProjectsList(new ArrayList<>());

        Project project = new Project();
        project.setClient(client);
        project.setExpirationDate("2023-15-03");
        project.setId(1L);
        project.setProjectName("Разработване на С-ма за следене на времето");

        ArrayList<Project> projectList = new ArrayList<>();
        projectList.add(project);
        when(projectRepository.findAll()).thenReturn(projectList);

        Client client1 = new Client();
        client1.setClientName("LudogorieSoft");
        client1.setId(1L);
        client1.setProjectsList(new ArrayList<>());

        Project project1 = new Project();
        project1.setClient(client1);
        project1.setExpirationDate("2023-15-03");
        project1.setId(1L);
        project1.setProjectName("Разработване на С-ма за следене на времето");
        assertTrue(clientProjectService.ifProjectExists(project1));
        verify(projectRepository).findAll();
    }

    @Test
    void testIfProjectExists3() {
        Client client = new Client();
        client.setClientName("LudogorieSoft");
        client.setId(1L);
        client.setProjectsList(new ArrayList<>());

        Project project = new Project();
        project.setClient(client);
        project.setExpirationDate("2023-15-03");
        project.setId(1L);
        project.setProjectName("Разработване на С-ма за следене на времето");

        Client client1 = new Client();
        client1.setClientName("Mr John Smith");
        client1.setId(2L);
        client1.setProjectsList(new ArrayList<>());

        Project project1 = new Project();
        project1.setClient(client1);
        project1.setExpirationDate("2020/03/01");
        project1.setId(2L);
        project1.setProjectName("com.example.newDesignApp.entity.Project");

        ArrayList<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project);
        when(projectRepository.findAll()).thenReturn(projectList);

        Client client2 = new Client();
        client2.setClientName("LudogorieSoft");
        client2.setId(1L);
        client2.setProjectsList(new ArrayList<>());

        Project project2 = new Project();
        project2.setClient(client2);
        project2.setExpirationDate("2023-15-03");
        project2.setId(1L);
        project2.setProjectName("Разработване на С-ма за следене на времето");
        assertTrue(clientProjectService.ifProjectExists(project2));
        verify(projectRepository).findAll();
    }

    @Test
    void testGetClientById() {
        Client client = new Client();
        client.setClientName("LudogorieSoft");
        client.setId(1L);
        client.setProjectsList(new ArrayList<>());
        Optional<Client> ofResult = Optional.of(client);
        when(clientRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(client, clientProjectService.getClientById(1L));
        verify(clientRepository).findById((Long) any());
    }

    @Test
    void testGetClientById2() {
        when(clientRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertTrue(clientProjectService.getClientById(1L).getProjectsList().isEmpty());
        verify(clientRepository).findById((Long) any());
    }
}

