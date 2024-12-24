package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service        // Could also use @Component. @Service gives more info.
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        // Check if email exists, save. Otherwise, throw exception.
        Optional<Student> studentWithThisEmail = studentRepository
                .findStudentByEmail(student.getEmail());

        if (studentWithThisEmail.isPresent()) {
            throw new IllegalStateException("Student with this email already exists");
        }

        // Email can be used, save student.
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }

    // This entity class Student
    // goes into manage state by using @Transactional.
    // Directly manage database by manipulating the class.
    @Transactional
    public void updateStudent(Long studentId,
                              String name,
                              String email) {
        // Make sure that student with this id exists.
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student with id " + studentId + " does not exist"));

        if (name != null &&
                !name.isEmpty() &&
                !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (email != null &&
                !email.isEmpty() &&
                !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentWithThisEmail = studentRepository
                    .findStudentByEmail(email);

            if (studentWithThisEmail.isPresent()) {
                throw new IllegalStateException(
                        "Student with email " + email + " already exists."
                );
            }
            student.setEmail(email);
        }
    }
}
