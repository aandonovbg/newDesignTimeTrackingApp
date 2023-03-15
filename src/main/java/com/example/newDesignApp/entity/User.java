package com.example.newDesignApp.entity;

import com.example.newDesignApp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Z\\d\\s]+$", message = "Моля,използвайте Латиница")
    private String username;

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[а-яА-Я\\s\\d]+", message = "Моля,използвайте Кирилица")
    @Column(name = "employee_full_name")
    private String fullName;

    @NotBlank
    @Email()
    private String email;

    @NotBlank
    //@Size(min =4,max =20)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.valueOf("EMPLOYEE");

    private boolean enabled;



    @Nullable
    @Lob
    private byte[] profilePicture;



    @Nullable
    @Pattern(regexp = "[а-яА-Я\\s\\d]+", message = "Моля,използвайте Кирилица")
    private String jobTitle;

    @Nullable
    @Pattern(regexp = "[а-яА-Я\\s\\d.,'\"]+", message = "Моля,използвайте Кирилица")
    private String address;

    @Nullable
    @Pattern(regexp = "[а-яА-Я\\s\\d]+", message = "Моля,използвайте Кирилица")
    private String city;

    @NotNull
    @Pattern(regexp = "^[0-9]+$", message = "позволени са 0123456789")
    private String salary;

    @Nullable
    @Pattern(regexp = "^[0-9+]+$", message = "+35912345678")
    private String GSM;


    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
