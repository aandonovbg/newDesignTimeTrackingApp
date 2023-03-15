package com.example.newDesignApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.newDesignApp.entity.Client;
import com.example.newDesignApp.entity.DailyProtocol;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.Project;
import com.example.newDesignApp.enums.Role;
import com.example.newDesignApp.repository.ClientRepository;
import com.example.newDesignApp.repository.DailyProtocolRepository;
import com.example.newDesignApp.repository.EmployeeRepository;

import java.io.UnsupportedEncodingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {Statistics.class})
@ExtendWith(SpringExtension.class)
class StatisticsTest {
    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private DailyProtocolRepository dailyProtocolRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private Statistics statistics;

    @Test
    void testGetEmployeeProtocols() throws UnsupportedEncodingException {
        Employee employee = new Employee();
        employee.setAddress("Цар Осв");
        employee.setCity("Тарго");
        ArrayList<DailyProtocol> dailyProtocolList = new ArrayList<>();
        employee.setDailyProtocol(dailyProtocolList);
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Антон Андонов");
        employee.setGSM("GSM");
        employee.setId(1L);
        employee.setJobTitle("шеф");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("5000");
        employee.setUsername("aandonov");
        when(employeeRepository.getEmployeeByUsername((String) any())).thenReturn(employee);
        List<DailyProtocol> actualEmployeeProtocols = statistics.getEmployeeProtocols("Name");
        assertSame(dailyProtocolList, actualEmployeeProtocols);
        assertTrue(actualEmployeeProtocols.isEmpty());
        verify(employeeRepository).getEmployeeByUsername((String) any());
    }

    @Test
    void testIfEmployeeExists() throws UnsupportedEncodingException {
        Employee employee = new Employee();
        employee.setAddress("Цар Осв");
        employee.setCity("Тарго");
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Антон Андонов");
        employee.setGSM("GSM");
        employee.setId(1L);
        employee.setJobTitle("шеф");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("5000");
        employee.setUsername("aandonov");
        when(employeeRepository.getEmployeeByUsername((String) any())).thenReturn(employee);
        assertTrue(statistics.ifEmployeeExists("Employee Name"));
        verify(employeeRepository).getEmployeeByUsername((String) any());
    }

    @Test
    void testIfEmployeeExistsThrows() {
        when(employeeRepository.getEmployeeByUsername((String) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> statistics.ifEmployeeExists("Employee Name"));
        verify(employeeRepository).getEmployeeByUsername((String) any());
    }

    @Test
    void testGetEmployeeFullName() throws UnsupportedEncodingException {
        Employee employee = new Employee();
        employee.setAddress("Цар Осв");
        employee.setCity("Тарго");
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Антон Андонов");
        employee.setGSM("GSM");
        employee.setId(1L);
        employee.setJobTitle("шеф");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("5000");
        employee.setUsername("aandonov");
        when(employeeRepository.getEmployeeByUsername((String) any())).thenReturn(employee);
        assertEquals("Антон Андонов", statistics.getEmployeeFullName("aandonov"));
        verify(employeeRepository).getEmployeeByUsername((String) any());
    }


    @Test
    void testSumTotalHoursEmployee() throws UnsupportedEncodingException {
        Employee employee = new Employee();
        employee.setAddress("Цар Осв");
        employee.setCity("Тарго");
        employee.setEmail("xxx@xxx.bg");
        employee.setEnabled(true);
        employee.setFullName("Антон Андонов");
        employee.setGSM("GSM");
        employee.setId(1L);
        employee.setJobTitle("шеф");
        employee.setPassword("1234");
        employee.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        employee.setRole(Role.EMPLOYEE);
        employee.setSalary("5000");
        employee.setUsername("aandonov");
        when(employeeRepository.getEmployeeByUsername((String) any())).thenReturn(employee);
        assertEquals(0, statistics.sumTotalHoursEmployee("aandonov"));
        verify(employeeRepository).getEmployeeByUsername((String) any());
    }

    @Test
    public void testGetMondayDate() {

        Date expectedDate = null;
        int weekNumber = 12;
        try {
            expectedDate = new SimpleDateFormat("dd.MM.yyyy").parse("20.03.2023");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date actualDate = statistics.getMondayDate(weekNumber);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    void testGetSundayDate() {
        Date expectedDate = null;
        int weekNumber = 11;
        try {
            expectedDate = new SimpleDateFormat("dd.MM.yyyy").parse("19.03.2023");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date actualDate = statistics.getSundayDate(weekNumber);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetWeekCount() {
        int expectedWeekCount = 52;
        int actualWeekCount = statistics.getWeekCount();
        assertEquals(expectedWeekCount, actualWeekCount);
    }

    @Test
    public void testValidateWeekNumber() {
        assertTrue(statistics.validateWeekNumber("1"));
        assertTrue(statistics.validateWeekNumber("52"));
        assertFalse(statistics.validateWeekNumber("-1"));
        assertFalse(statistics.validateWeekNumber("53"));
    }

    @Test
    void testCollectProtocolsFromSpecificWeek() {
        when(dailyProtocolRepository.findAll()).thenReturn(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date monday = Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertTrue(statistics
                .collectProtocolsFromSpecificWeek(monday, Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()))
                .isEmpty());
        verify(dailyProtocolRepository).findAll();
    }

    @Test
    void testSumTotalHours() {
        when(dailyProtocolRepository.findAll()).thenReturn(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date monday = Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertEquals(0,
                statistics.sumTotalHours(monday, Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant())));
        verify(dailyProtocolRepository).findAll();
    }

    @Test
    void testSumTotalHoursThrows() {
        when(dailyProtocolRepository.findAll()).thenThrow(new RuntimeException());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date monday = Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(RuntimeException.class,
                () -> statistics.sumTotalHours(monday, Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant())));
        verify(dailyProtocolRepository).findAll();
    }

    @Test
    void testGetEmployeesCount() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, statistics.getEmployeesCount());
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetEmployeesCountThrows() {
        when(employeeRepository.findAll()).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> statistics.getEmployeesCount());
        verify(employeeRepository).findAll();
    }


    @Test
    void testGetClientCount() {
        when(clientRepository.findAll()).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> statistics.getClientCount());
        verify(clientRepository).findAll();
    }
    @Test
    void testGetClientCount2() {
        when(clientRepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, statistics.getClientCount());
        verify(clientRepository).findAll();
    }
}

