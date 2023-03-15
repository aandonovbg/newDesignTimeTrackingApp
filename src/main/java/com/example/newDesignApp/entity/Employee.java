package com.example.newDesignApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee extends User {
    @JsonIgnore // ignore this field because it has bidirectional relationship with another entity
    @Column(name = "employee_daily_protocol", nullable = true)
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true) //1 employee can have multiple daily protocols
    private List<DailyProtocol> dailyProtocol=new ArrayList<>();
}
