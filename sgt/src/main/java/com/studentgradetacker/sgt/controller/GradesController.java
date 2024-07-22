package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.dto.custom_DTO.CourseGradeDTO;
import com.studentgradetacker.sgt.dto.custom_DTO.StudentDetailsDTO;
import com.studentgradetacker.sgt.dto.custom_DTO_mapper.CourseGradeDTOMapper;
import com.studentgradetacker.sgt.dto.custom_DTO_mapper.StudentDetailsDTOMapper;
import com.studentgradetacker.sgt.model.Enrolled;
import com.studentgradetacker.sgt.model.Grades;
import com.studentgradetacker.sgt.model.Students;
import com.studentgradetacker.sgt.model.payload.request.AddGradesRequest;
import com.studentgradetacker.sgt.model.payload.request.UpdateGradeRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.model.payload.response.StudentGradeResponse;
import com.studentgradetacker.sgt.repository.EnrolledRepository;
import com.studentgradetacker.sgt.repository.GradesRepository;
import com.studentgradetacker.sgt.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sgt/grades")
public class GradesController {

    @Autowired
    GradesRepository gradesRepository;

    @Autowired
    EnrolledRepository enrolledRepository;

    @Autowired
    StudentsRepository studentsRepository;

    @Autowired
    CourseGradeDTOMapper courseGradeDTOMapper;

    @Autowired
    StudentDetailsDTOMapper studentDetailsDTOMapper;

    @GetMapping
    public ResponseEntity<?> getAllGrades() {

        List<Grades> grades = gradesRepository.findByIsArchivedFalse();

        if (grades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse("There are no grades found!"));
        }
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/{gradeId}")
    public ResponseEntity<?> getGradeByGradeId(@PathVariable Integer gradeId) {
        Grades grades = gradesRepository.findByGradeIdAndIsArchivedFalse(gradeId);

        if(grades == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("There is no grades found for gradeId: %d!", gradeId)));
        }
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getGradesByStudentId(@PathVariable Integer studentId) {
        Students existingStudent = studentsRepository.findByStudentIdAndIsArchivedFalse(studentId);
        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
        List<Grades> grades = gradesRepository.findByStudentId(studentId);

        StudentDetailsDTO studentDetailsDTO = studentDetailsDTOMapper.apply(existingStudent);


        List<CourseGradeDTO> courseGradeDTOs = grades.stream()
                .map(courseGradeDTOMapper)
                .toList();

        return ResponseEntity.ok(new StudentGradeResponse(studentDetailsDTO, courseGradeDTOs));

    }

    @GetMapping("/archived")
    public ResponseEntity<?> getAllArchivedGrades() {

        List<Grades> grades = gradesRepository.findByIsArchivedTrue();
        if (grades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse("There are no archived grades!"));
        }
        return ResponseEntity.ok(grades);
    }

    @PostMapping
    public ResponseEntity<?> addGrades(@RequestBody AddGradesRequest addGradesRequest) {

        Integer enrolledId = addGradesRequest.getEnrolledId();

        Enrolled enrolled = enrolledRepository.findByEnrolledIdAndIsArchivedFalse(enrolledId);
        if(enrolled == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("There are is no enrollee with enrolledId: %d", enrolledId)));
        }

        Grades existingGrades = gradesRepository.findByEnrolledAndIsArchivedFalse(enrolled);
        if (existingGrades != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new MessageResponse(
                            String.format("Grades already exist for enrollee with enrolledId: %d. Update grades instead", enrolledId)));
        }

        Grades grades = new Grades(
                enrolled,
                addGradesRequest.getPrelims(),
                addGradesRequest.getMidterms(),
                addGradesRequest.getFinals()
        );

        gradesRepository.save(grades);

        return ResponseEntity.ok(new MessageResponse(
                String.format("Grades for Enrollee with enrolledId: %d have been added", enrolledId)
        ));
    }

    @PutMapping("/{gradeId}")
    public ResponseEntity<?> updateGrades(@RequestBody UpdateGradeRequest updateGradeRequest, @PathVariable Integer gradeId) {

        Grades grades = gradesRepository.findByGradeIdAndIsArchivedFalse(gradeId);
        if (grades == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("There are no grades found for gradeId: %d!", gradeId)));
        }
        grades.updateGrades(
                updateGradeRequest.getPrelims(),
                updateGradeRequest.getMidterms(),
                updateGradeRequest.getFinals()
        );

        gradesRepository.save(grades);

        return ResponseEntity.ok(new MessageResponse(
                String.format("Grades for gradeId: %d have been updated", gradeId)
        ));
    }

    @PutMapping("/archive/{gradeId}")
    public ResponseEntity<?> archiveGrade(@PathVariable Integer gradeId) {

        Grades grades = gradesRepository.findByGradeIdAndIsArchivedFalse(gradeId);
        if(grades == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("There is no grades found for gradeId: %d!", gradeId)));
        }

        grades.setIsArchived(Boolean.TRUE);
        gradesRepository.save(grades);

        return ResponseEntity.ok(
                new MessageResponse(
                        String.format("Grades has been archived successfully for gradeId: %d", gradeId)));
    }

    @DeleteMapping("/{gradeId}")
    public ResponseEntity<?> deleteGrade(@PathVariable Integer gradeId) {
        Grades grades = gradesRepository.findByGradeIdAndIsArchivedTrue(gradeId);
        if(grades == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("There is no archive grades found for gradeId: %d!", gradeId)));
        }
        gradesRepository.delete(grades);
        return ResponseEntity.ok(
                new MessageResponse(
                        String.format("Grades has been deleted successfully for gradeId: %d", gradeId)));

    }
}
