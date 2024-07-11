package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Students;
import com.studentgradetacker.sgt.model.payload.request.StudentRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.respository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sgt/students")
public class StudentsController {

    @Autowired
    StudentsRepository studentsRepository;

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        List<Students> allStudents = studentsRepository.findAll();

        if (allStudents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allStudents);
    };

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentByStudentId(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid student ID"));
        }

        // Fetch the student by ID from repository
        Students existingStudent = studentsRepository.findByStudentId(id);

        if (existingStudent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(existingStudent);
    }

    @PostMapping("/addStudent")
    public ResponseEntity<?> addNewStudent(@RequestBody StudentRequest addStudentRequest) {
        Students student = new Students(addStudentRequest.getFirstName(), addStudentRequest.getLastName(), addStudentRequest.getEmail());
        studentsRepository.save(student);
        return ResponseEntity.ok(new MessageResponse("Student has been added successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentDetails(@RequestBody StudentRequest updateStudentRequest, @PathVariable Integer id) {
        Optional<Students> studentOptional = Optional.ofNullable(studentsRepository.findByStudentId(id));

        if (!studentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Students existingStudent = studentOptional.get();

        existingStudent.setFirstName(updateStudentRequest.getFirstName());
        existingStudent.setLastName(updateStudentRequest.getLastName());
        existingStudent.setEmail(updateStudentRequest.getEmail());

        studentsRepository.save(existingStudent);

        return ResponseEntity.ok(new MessageResponse("Student details updated successfully!"));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {

        Students existingStudent = studentsRepository.findByStudentId(id);

        if (existingStudent == null) {
            return ResponseEntity.notFound().build();
        }

        studentsRepository.delete(existingStudent);

        return ResponseEntity.ok(new MessageResponse("Student deleted successfully!"));

    }
}
