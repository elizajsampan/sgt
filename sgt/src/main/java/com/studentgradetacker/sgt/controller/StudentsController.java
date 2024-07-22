package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.dto.custom_DTO.EnrolledStudentDTO;
import com.studentgradetacker.sgt.dto.custom_DTO.StudentGradesDTO;
import com.studentgradetacker.sgt.model.Students;
import com.studentgradetacker.sgt.model.payload.request.StudentRequest;
import com.studentgradetacker.sgt.model.payload.response.GwaResponse;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.repository.StudentsRepository;
import com.studentgradetacker.sgt.service.StudentsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/sgt/students")
public class StudentsController {

    private static final String EMAIL_REGEX = "^(?=.*[a-zA-Z0-9._%+-])[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @Autowired
    StudentsRepository studentsRepository;

    @Autowired
    StudentsService studentsService;

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        List<Students> allStudents = studentsRepository.findByIsArchivedFalse();

        if (allStudents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There are no students"));
        }

        return ResponseEntity.ok(allStudents);
    };

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentByStudentId(@PathVariable Integer studentId) {
        if (studentId == null || studentId <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId: " + studentId));
        }

        // Fetch the student by ID from repository
        Students existingStudent = studentsRepository.findByStudentIdAndIsArchivedFalse(studentId);

        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId: " + studentId));
        }

        return ResponseEntity.ok(existingStudent);
    }

    @GetMapping("/{studentId}/gwa")
    public ResponseEntity<?> getStudentGWA(@PathVariable Integer studentId) {
        Students student = studentsRepository.findByStudentIdAndIsArchivedFalse(studentId);
        Double gwa = studentsService.calculateGWA(studentId);
        if (gwa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No GWA found for student.");
        }
        return ResponseEntity.ok(new GwaResponse(student, gwa));
    }

    @GetMapping("/archived")
    public ResponseEntity<?> getAllArchivedStudents() {
        List<Students> allArchivedStudents = studentsRepository.findByIsArchivedTrue();

        if (allArchivedStudents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There are no archived students"));
        }

        return ResponseEntity.ok(allArchivedStudents);
    }

    @PostMapping
    public ResponseEntity<?> addNewStudent(@Valid @RequestBody StudentRequest addStudentRequest) {

        if(addStudentRequest.getFirstName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Firstname cannot be empty"));
        }
        if(addStudentRequest.getLastName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Lastname cannot be empty"));
        }
        if(addStudentRequest.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email cannot be empty"));
        }
        if(!pattern.matcher(addStudentRequest.getEmail()).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email should be valid"));
        }

        Students student = new Students(addStudentRequest.getFirstName(), addStudentRequest.getLastName(), addStudentRequest.getEmail());
        studentsRepository.save(student);
        return ResponseEntity.ok(new MessageResponse("Student has been added successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentDetails(@RequestBody StudentRequest updateStudentRequest, @PathVariable Integer id) {
        Optional<Students> studentOptional = Optional.ofNullable(studentsRepository.findByStudentIdAndIsArchivedFalse(id));

        if (studentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId: " + id));
        }

        Students existingStudent = studentOptional.get();

        if (updateStudentRequest.getFirstName() != null && !updateStudentRequest.getFirstName().isEmpty()) {
            existingStudent.setFirstName(updateStudentRequest.getFirstName());
        }

        if (updateStudentRequest.getLastName() != null && !updateStudentRequest.getLastName().isEmpty()) {
            existingStudent.setLastName(updateStudentRequest.getLastName());
        }

        if (updateStudentRequest.getEmail() != null && !updateStudentRequest.getEmail().isEmpty()) {
            if (!pattern.matcher(updateStudentRequest.getEmail()).matches()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email should be valid"));
            }
            existingStudent.setEmail(updateStudentRequest.getEmail());
        }

        studentsRepository.save(existingStudent);

        return ResponseEntity.ok(new MessageResponse("Student details updated successfully!"));

    }

    //soft delete student record
    @PutMapping("/archive/{studentId}")
    public ResponseEntity<?> archiveStudent(@PathVariable Integer studentId) {
        Students existingStudent = studentsRepository.findByStudentIdAndIsArchivedFalse(studentId);

        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("Student with studentId: %d is not found!", studentId)));
        }

        existingStudent.setIsArchived(Boolean.TRUE);
        studentsRepository.save(existingStudent);

        return ResponseEntity.ok(new MessageResponse(
                String.format("Student with studentId: %d has been archived!", studentId)));
    }

    @PutMapping("/unarchive/{studentId}")
    public ResponseEntity<?> unarchiveStudent(@PathVariable Integer studentId) {

        Students archivedStudent = studentsRepository.findByStudentIdAndIsArchivedTrue(studentId);

        if(archivedStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("Student with studentId: %d is not found student in archives", studentId)));

        }
        archivedStudent.setIsArchived(Boolean.FALSE);
        studentsRepository.save(archivedStudent);

        return ResponseEntity.ok(new MessageResponse(
                String.format("Student with studentId: %d has been unarchived!", studentId)));
    }

    //hard delete student record, can only be deleted when archived
    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer studentId) {

        Students existingStudent = studentsRepository.findByStudentIdAndIsArchivedTrue(studentId);

        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("Student with studentId: %d is not found student in archives", studentId)));
        }

        studentsRepository.delete(existingStudent);

        return ResponseEntity.ok(new MessageResponse(
                String.format("Archived student with studentId: %d has been deleted successfully!", studentId)));

    }
}
