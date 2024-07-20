package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.dto.custom_DTO.EnrolledStudentDTO;
import com.studentgradetacker.sgt.dto.custom_DTO.StudentGradesDTO;
import com.studentgradetacker.sgt.model.Students;
import com.studentgradetacker.sgt.model.payload.request.StudentRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.respository.EnrolledRepository;
import com.studentgradetacker.sgt.respository.StudentsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        Students existingStudent = studentsRepository.findByStudentId(studentId);

        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId: " + studentId));
        }

        return ResponseEntity.ok(existingStudent);
    }

    @GetMapping("/archived")
    public ResponseEntity<?> getAllArchivedStudents() {
        List<Students> allArchivedStudents = studentsRepository.findByIsArchivedTrue();

        if (allArchivedStudents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There are no archived students"));
        }

        return ResponseEntity.ok(allArchivedStudents);
    }

    @GetMapping("/{studentId}/grades")
    public ResponseEntity<?> getStudentGrade(@PathVariable Integer studentId) {
        if (studentId == null || studentId == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId " + studentId));
        }

        Students existingStudent = studentsRepository.findByStudentId(studentId);
        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId " + studentId));
        }

        List<EnrolledStudentDTO> enrolledStudent = studentsRepository.findCoursesEnrolledByStudentId(studentId);
        System.out.println(enrolledStudent);
        if(enrolledStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Student with studentId: " + studentId + " is not enrolled!"));
        }

        List<StudentGradesDTO> studentGradesDTO = studentsRepository.findGradesByStudentId(studentId);
        if(studentGradesDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Student with studentId: " + studentId + " grades not found"));
        }
        return ResponseEntity.ok(studentGradesDTO);
    }

    @GetMapping("/enrolled/{studentId}")
    public ResponseEntity<?> getEnrolledCoursesByStudentId(@PathVariable Integer studentId) {
        if (studentId == null || studentId == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId " + studentId));
        }

        Students existingStudent = studentsRepository.findByStudentId(studentId);
        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId " + studentId));
        }

        List<EnrolledStudentDTO> enrolledStudent = studentsRepository.findCoursesEnrolledByStudentId(studentId);
        System.out.println(enrolledStudent);
        if(enrolledStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Student with studentId: " + studentId + " is not enrolled!"));
        }

        return ResponseEntity.ok(enrolledStudent);
    }


    @PostMapping("/addStudent")
    public ResponseEntity<?> addNewStudent(@Valid @RequestBody StudentRequest addStudentRequest, BindingResult bindingResult) {

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
        Optional<Students> studentOptional = Optional.ofNullable(studentsRepository.findByStudentId(id));

        if (studentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId: " + id));
        }
        if(updateStudentRequest.getFirstName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Firstname cannot be empty"));
        }
        if(updateStudentRequest.getLastName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Lastname cannot be empty"));
        }
        if(updateStudentRequest.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email cannot be empty"));
        }
        if(!pattern.matcher(updateStudentRequest.getEmail()).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email should be valid"));
        }

        Students existingStudent = studentOptional.get();

        existingStudent.setFirstName(updateStudentRequest.getFirstName());
        existingStudent.setLastName(updateStudentRequest.getLastName());
        existingStudent.setEmail(updateStudentRequest.getEmail());
        existingStudent.setIsArchived(Boolean.FALSE);

        studentsRepository.save(existingStudent);

        return ResponseEntity.ok(new MessageResponse("Student details updated successfully!"));

    }

    //soft delete student record
    @PutMapping("/archive/{id}")
    public ResponseEntity<?> archiveStudent(@PathVariable Integer id) {
        Students existingStudent = studentsRepository.findByStudentId(id);

        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId: " + id));
        }

        existingStudent.setIsArchived(Boolean.TRUE);

        return ResponseEntity.ok(new MessageResponse("Student has been archived!"));
    }

    //hard delete student record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {

        Students existingStudent = studentsRepository.findByStudentId(id);

        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId: " + id));
        }

        studentsRepository.delete(existingStudent);

        return ResponseEntity.ok(new MessageResponse("Student deleted successfully!"));

    }
}
