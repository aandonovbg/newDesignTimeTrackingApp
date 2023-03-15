package com.example.newDesignApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {


    @Size(min =2,max =50)
    @Pattern(regexp = "[a-zA-Z]+",message = "Моля,използвайте Латиница")
    @Nullable
    private String employeeName;

    @Nullable
    @Size(min = 1, max = 2,message = "Диапазонът е 1-52(53)")
    private String weekNumber;
}
