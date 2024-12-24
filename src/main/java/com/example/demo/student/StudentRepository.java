package com.example.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository     // This interface is responsible for data access.
public interface StudentRepository
        extends JpaRepository<Student,Long> {   // Type of class and ID

    // Jpa query language.
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmail(String email);
}
