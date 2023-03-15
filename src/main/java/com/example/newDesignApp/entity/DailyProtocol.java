package com.example.newDesignApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;


@Data //adds getters and setters
@Entity
@Builder
@NoArgsConstructor //generates a constructor with no arguments
@AllArgsConstructor //generates a constructor that takes all fields from the class
@Table(name = "daily_protocol")
public class DailyProtocol {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private String protocolDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id",referencedColumnName = "id",nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @NotNull
    @Min(1)
    @Max(value = 8, message = "Времевият диапазон е между 1 и 8 часа включително")
    @Column(name = "time_worked")
    private int timeWorked;

    @NotBlank
    @Size(min = 2 , max =200)
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s\\d.,!?()-_]+")
    private String description;

}
