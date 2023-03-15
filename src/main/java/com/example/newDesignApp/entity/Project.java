package com.example.newDesignApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data //adds getters and setters
@Entity
@Builder
@NoArgsConstructor //generates a constructor with no arguments
@AllArgsConstructor //generates a constructor that takes all fields from the class
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min =2,max =200)
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s\\d.,!?()-_]+")
    private String projectName;

    @NotBlank
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "expiration_date")
    private String expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;
}
