package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ')")
    public Student getStudentById(@PathVariable Long id, Authentication authentication) {
        return studentService.getStudentById(id, authentication);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Student> createStudent(
            @RequestBody Student student,
            Authentication authentication) {
        Student createdStudent = studentService.createStudent(student, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student, Authentication authentication) {
        return studentService.updateStudent(id, student, authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id, Authentication authentication) {
        studentService.deleteStudent(id, authentication);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ')")
    public List<Student> getAllStudents(Authentication authentication) {
        return studentService.getAllStudents(authentication);
    }
}