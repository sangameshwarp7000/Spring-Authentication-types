package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.entity.CreatedBy;
import org.springframework.security.core.Authentication;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostAuthorize("hasRole('ADMIN') or returnObject.createdBy == T(com.example.demo.entity.CreatedBy).USER")
    public Student getStudentById(Long id, Authentication authentication) {
        requireStudentReadRole(authentication);

        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not found"));
    }

    public List<Student> getAllStudents(Authentication authentication) {
        requireStudentReadRole(authentication);

        if (hasRole(authentication, "ADMIN")) {
            return studentRepository.findAll();
        }

        return studentRepository.findAllByCreatedBy(CreatedBy.USER);
    }

    private void requireStudentReadRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Authentication is required");
        }

        if (!hasRole(authentication, "ADMIN") && !hasRole(authentication, "USER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "ADMIN or USER role is required");
        }
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }

    public Student createStudent(Student student, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

        if (isAdmin) {
            student.setCreatedBy(CreatedBy.ADMIN);
        } else if (isUser) {
            student.setCreatedBy(CreatedBy.USER);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ADMIN or USER role is required");
        }

        student.setId(null);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student, Authentication authentication) {
        Student existingStudent = getStudentById(id, authentication);
        existingStudent.setName(student.getName());
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id, Authentication authentication) {
        Student student = getStudentById(id, authentication);
        studentRepository.delete(student);
    }

}