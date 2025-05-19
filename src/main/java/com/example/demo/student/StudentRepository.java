package com.example.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//repoistory is responsabile for data access, so data access layer is here (repository)
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

//    this will search in db from the input we gave it, select * from student where email = ?
    @Query("SELECT s from Student s where s.email = ?1")
    Optional<Student> findStudentByEmail(String email);
}
