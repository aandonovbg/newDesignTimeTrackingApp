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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class Statistics {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<DailyProtocol> getEmployeeProtocols(String name) {

        Employee employee = employeeRepository.getEmployeeByUsername(name);

        return employee.getDailyProtocol();
    }

    public boolean ifEmployeeExists(String employeeName){
        boolean exists = false;
        Employee employee=new Employee();

            Optional<Employee> optionalEmployee = Optional.ofNullable((employeeRepository.getEmployeeByUsername(employeeName)));
            if (optionalEmployee.isPresent()) {
                exists=true;
            }

        return exists;
    }
    public String getEmployeeFullName(String employeeUserName) {
        Employee employee=employeeRepository.getEmployeeByUsername(employeeUserName);
        return employee.getFullName();
    }
    public int sumTotalHoursEmployee(String employeeUserName){
        int total=0;
        List<DailyProtocol> employeeProtocols=getEmployeeProtocols(employeeUserName);
        for (DailyProtocol employeeProtocol:employeeProtocols) {
            total+=employeeProtocol.getTimeWorked();
        }
        return total;
    }

    // ************************** WEEK NUMBER **************************

    public Date getMondayDate(int weekNumber) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getSundayDate(int weekNumber) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        try {
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DailyProtocol> getSpecificEmployeeWeekProtocols(Long employeeId){
        List<DailyProtocol> allWeekProtocols=collectProtocolsFromSpecificWeek(getMondayDate(getCurrentWeekNumber()),getSundayDate(getCurrentWeekNumber()));
        List<DailyProtocol> employeeWeekProtocols=new ArrayList<>();
        for (DailyProtocol protocol:allWeekProtocols) {
            if (protocol.getEmployee().getId() == employeeId){
                employeeWeekProtocols.add(protocol);
            }
        }
        return employeeWeekProtocols;
    }
    public int getCurrentWeekNumber(){
        LocalDate date = LocalDate.now();

        return date.get(WeekFields.ISO.weekOfWeekBasedYear());
    }
    public int getWeekCount() {
        Calendar calendar = Calendar.getInstance();

        return calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);
    }
    public boolean validateWeekNumber(String weekNum){
        boolean isValid=true;
        int weekNumber=Integer.parseInt(weekNum);
        Calendar calendar = Calendar.getInstance();
        int weekCount = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);

        if (weekNumber < 0 || weekNumber > weekCount) {
            isValid = false;
        }
        return isValid;
    }

    public List<DailyProtocol> collectProtocolsFromSpecificWeek(Date monday, Date sunday) {
        List<DailyProtocol> weekProtocols = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        List<DailyProtocol> allDailyProtocols= dailyProtocolRepository.findAll();

        for (DailyProtocol dailyProtocol : allDailyProtocols) {
            try {
                Date date=sdf.parse(dailyProtocol.getProtocolDate());
                if (date.compareTo(monday) >= 0 && date.compareTo(sunday) <= 0){
                    weekProtocols.add(dailyProtocol);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return weekProtocols;
    }

    public int sumTotalHours(Date monday,Date sunday){
        int total=0;
        List<DailyProtocol> allDailyProtocols=collectProtocolsFromSpecificWeek(monday,sunday);
        for (DailyProtocol employeeProtocol:allDailyProtocols) {
            total+=employeeProtocol.getTimeWorked();
        }
        return total;
    }

    public int getEmployeesCount(){
        List<Employee> employees = employeeRepository.findAll();
        return employees.size();
    }

    public int getClientCount() {
        List<Client> clients = (List<Client>) clientRepository.findAll();
        return clients.size();
    }

    public List<Client> getAllClientsList(){
        return (List<Client>) clientRepository.findAll();
    }
    public int getProjectsCount(){
        List<Project> projects=projectRepository.findAll();
        return projects.size();
    }
}
