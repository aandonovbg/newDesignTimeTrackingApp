package com.example.newDesignApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data //adds getters and setters
@NoArgsConstructor //generates a constructor with no arguments
@AllArgsConstructor //generates a constructor that takes all fields from the class
public class ClientProjectDto {

    @NotBlank
    @Size(min =2,max =50)
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s\\d.,!?()-_]+")
    private String clientName;

    @NotBlank
    @Size(min =2,max =200)
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s\\d.,!?()-_]+")
    private String projectName;

    @NotBlank
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "expiration_date")
    private String expirationDate;
}
