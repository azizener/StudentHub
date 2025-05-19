package com.example.demo.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDTO {
    private String name;
    @NotBlank(message = "Enter email!")
    @Email(message = "Wrong email forum!")
    private String email;
    private LocalDate dob;
}
