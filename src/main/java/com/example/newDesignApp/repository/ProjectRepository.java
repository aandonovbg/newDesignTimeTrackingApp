package com.example.newDesignApp.repository;

import com.example.newDesignApp.entity.Client;
import com.example.newDesignApp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p.client FROM Project p WHERE p.id = :projectId")
    Client getClientByProjectId(@Param("projectId") Long projectId);
}
