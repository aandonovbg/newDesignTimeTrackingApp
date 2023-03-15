package com.example.newDesignApp.dto;

import com.example.newDesignApp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    @NotBlank
    @Size(min =2,max =50)
    @Pattern(regexp = "[a-zA-Z]+",message = "Моля,използвайте Латиница")
    private String username;

    @NotBlank
    @Size(min =3,max =50)
    @Pattern(regexp = "[а-яА-Я\\s\\d]+",message = "Моля,използвайте Кирилица")
    private String fullName;

    @NotBlank
    @Email()
    private String email;

    @NotBlank
    //@Size(min =4,max =20)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;

    @Transient
    private MultipartFile imageFile;

    @NotNull
    @Pattern(regexp = "^[0-9]+$", message = "позволени са 0123456789")
    private String salary;


    @Nullable
    @Pattern(regexp = "[а-яА-Я\\s\\d]+", message = "Моля,използвайте Кирилица")
    private String jobTitle;

    @Nullable
    @Pattern(regexp = "^[0-9+]+$", message = "+35912345678")
    private String GSM;
}
