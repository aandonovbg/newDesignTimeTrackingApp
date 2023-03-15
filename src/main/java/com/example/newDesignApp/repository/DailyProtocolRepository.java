package com.example.newDesignApp.repository;

import com.example.newDesignApp.entity.DailyProtocol;
import com.example.newDesignApp.entity.Employee;
import com.example.newDesignApp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyProtocolRepository extends JpaRepository<DailyProtocol, Long> {
    List<DailyProtocol> getProtocolByProtocolDate(String protocolDate);

    @Query("SELECT d FROM DailyProtocol d WHERE d.employee.id = :employeeId AND d.protocolDate = :protocolDate")
    List<DailyProtocol> findBySpecificEmployeeIdAndProtocolDate(@Param("employeeId") Long employeeId, @Param("protocolDate") String protocolDate);

    @Query("SELECT COUNT(DISTINCT dp.employee.id) FROM DailyProtocol dp WHERE dp.client.id = :clientId")
    int countDistinctEmployeesByClient(@Param("clientId") Long clientId);

    @Query("SELECT DISTINCT dp.employee FROM DailyProtocol dp WHERE dp.client.id = :clientId")
    List<Employee> getListOfDistinctEmployeesByClient(@Param("clientId") Long clientId);

    @Query("SELECT DISTINCT d.project FROM DailyProtocol d WHERE d.employee.id = :employeeId")
    List<Project> getDistinctProjectsListByEmployeeId(@Param("employeeId") Long employeeId);

}
