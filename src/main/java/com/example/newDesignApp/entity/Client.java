package com.example.newDesignApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data //adds getters and setters
@Entity
@Builder
@NoArgsConstructor //generates a constructor with no arguments
@AllArgsConstructor //generates a constructor that takes all fields from the class
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min =2,max =50)
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s\\d.,!?()-_]+")
    private String clientName;

    @JsonIgnore // ignore this field because it has bidirectional relationship with another entity
    @Column(name = "client_project", nullable = true)
    @OneToMany(mappedBy = "client") //1 client can have multiple projects
    private List<Project> projectsList=new ArrayList<>();

}
