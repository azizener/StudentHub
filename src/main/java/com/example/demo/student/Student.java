package com.example.demo.student;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table
public class Student {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private long id;
    private String name;
    private String email;
    private LocalDate dob;

    @Transient
    private Integer age;


    public Student(LocalDate dob, String email, String name) {
        this.dob = dob;
        this.email = email;
        this.name = name;
    }


    public Student() {}
}