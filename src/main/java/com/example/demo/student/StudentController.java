package com.example.demo.student;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import static com.example.demo.constant.URLs.Student.*;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> getStudent() {
    return studentService.getStudents();
    }


//    for every post requst theres a validate for user request body, params
//    @PostMapping(ADD)
//    public ResponseEntity<String> registerNewStudent(@RequestBody @Valid StudentDTO student){
//        try {
//        studentService.addNewStudent(student);
//        return ResponseEntity.status(201).body("Student added");
//        }catch (IllegalStateException e){
//            return ResponseEntity.status(409).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(501).body(e.getMessage());
//        }
//    }


    @PostMapping(ADD)
    public void registerNewStudent(@RequestBody @Valid StudentDTO student){
        studentService.addNewStudent(student);
    }

    @DeleteMapping(BY_ID)
    public void delStudent(@PathVariable Long studentId){
    studentService.delStudent(studentId);
    }


    @PutMapping(BY_ID)
    public void editStudent(@PathVariable Long studentId, @RequestBody Student student) {
        studentService.editStudent(studentId, student);
    }

}