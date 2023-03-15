package com.example.newDesignApp.repository;

import com.example.newDesignApp.entity.Client;
import com.example.newDesignApp.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client,Long> {
    public Client getClientByClientName(String clientName);
}
