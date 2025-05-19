package com.example.demo.student;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//@Component means that this class is beaned into other class or better to say it it has been associated with it
//Component works fine it is a general component, but if its a more a specified for a specific business then we shall call it @Service

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(StudentDTO student) {
        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
        if (studentByEmail.isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        studentRepository.save(new Student(student.getDob(), student.getEmail(), student.getName()));
    }

    public void delStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);

        if (!exists) {
            throw new IllegalStateException("Student" + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }

//   Transactional! Mostly used to prevent mid crashing, data loss
    @Transactional
    public void editStudent(Long studentId, Student updatedStudent) {
        Student existingStudent = studentRepository.findById(studentId)
        .orElseThrow(() -> new IllegalStateException("Student with ID " + studentId + " does not exist"));

        // Check if email is being changed to an existing one
        if (updatedStudent.getEmail() != null &&
                !updatedStudent.getEmail().equals(existingStudent.getEmail())) {
            Optional<Student> studentByEmail = studentRepository.findStudentByEmail(updatedStudent.getEmail());
            if (studentByEmail.isPresent()) {
                throw new IllegalStateException("Email already taken");
            }
            existingStudent.setEmail(updatedStudent.getEmail());
        }

        if (updatedStudent.getName() != null) {
            existingStudent.setName(updatedStudent.getName());
        }

        if (updatedStudent.getDob() != null) {
            existingStudent.setDob(updatedStudent.getDob());
        }

        // No need to call save() because @Transactional + entity is managed
    }
}