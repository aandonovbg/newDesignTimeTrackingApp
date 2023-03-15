package com.example.newDesignApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //adds getters and setters
@NoArgsConstructor //generates a constructor with no arguments
@AllArgsConstructor //generates a constructor that takes all fields from the class
public class ClientDataDto {
    private String clientName;
    private int projectsCount;
}
