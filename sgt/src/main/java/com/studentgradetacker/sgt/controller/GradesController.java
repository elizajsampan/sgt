package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Enrolled;
import com.studentgradetacker.sgt.model.Grades;
import com.studentgradetacker.sgt.model.payload.request.AddGradesRequest;
import com.studentgradetacker.sgt.model.payload.request.UpdateGradeRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.respository.EnrolledRepository;
import com.studentgradetacker.sgt.respository.GradesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/sgt/grades")
public class GradesController {

    @Autowired
    GradesRepository gradesRepository;

    @Autowired
    EnrolledRepository enrolledRepository;

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
                String.format("Grades for Enrollee with enrolledId: %d has been added", enrolledId)
        ));
    }

    @PutMapping("/{gradeId}")
    public ResponseEntity<?> updateGrades(@RequestBody UpdateGradeRequest updateGradeRequest, @PathVariable Integer gradeId) {

        Grades grades = gradesRepository.findByGradeIdAndIsArchivedFalse(gradeId);
        if(grades == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(
                            String.format("There is no grades found for gradeId: %d!", gradeId)));
        }

        boolean isUpdated = false;
        if(updateGradeRequest.getPrelims() != null && !(updateGradeRequest.getPrelims() == 0.0)) {
            grades.setPrelims(updateGradeRequest.getPrelims());
            isUpdated = true;
        }
        if(updateGradeRequest.getMidterms() != null && !(updateGradeRequest.getMidterms() == 0.0)) {
            grades.setMidterms(updateGradeRequest.getMidterms());
            isUpdated = true;
        }
        if(updateGradeRequest.getFinals() != null && !(updateGradeRequest.getFinals() == 0.0)) {
            grades.setFinals(updateGradeRequest.getFinals());
            isUpdated = true;
        }
        if (isUpdated) {
            double finalGrade = getFinalGrade(grades);

            BigDecimal roundedFinalGrade = BigDecimal.valueOf(finalGrade).setScale(2, RoundingMode.HALF_UP);
            grades.setFinalGrade(roundedFinalGrade.doubleValue());
            gradesRepository.save(grades);

            return ResponseEntity.ok(
                    new MessageResponse(
                            String.format("Grades updated successfully for gradeId: %d", gradeId)));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("No valid grades provided to update"));

    }

    private static double getFinalGrade(Grades grades) {
        double prelims = grades.getPrelims() != null ? grades.getPrelims() : 0.0;
        double midterms = grades.getMidterms() != null ? grades.getMidterms() : 0.0;
        double finals = grades.getFinals() != null ? grades.getFinals() : 0.0;

        int count = 0;
        if (grades.getPrelims() != null) count++;
        if (grades.getMidterms() != null) count++;
        if (grades.getFinals() != null) count++;

        double finalGrade = 0.0;
        if (count > 0) {
           finalGrade = ((prelims + midterms + finals) / count);
        }
        return finalGrade;
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
