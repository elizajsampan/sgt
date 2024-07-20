package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.Enrolled;
import com.studentgradetacker.sgt.model.Students;
import com.studentgradetacker.sgt.model.payload.request.EnrollmentRequest;
import com.studentgradetacker.sgt.model.payload.request.UpdateEnrolleeCourseRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.respository.CoursesRepository;
import com.studentgradetacker.sgt.respository.EnrolledRepository;
import com.studentgradetacker.sgt.respository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sgt/enrolled")
public class EnrolledController {

    @Autowired
    EnrolledRepository enrolledRepository;

    @Autowired
    StudentsRepository studentsRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @GetMapping
    public ResponseEntity<?> getAllEnrolled(){

        List<Enrolled> enrolledList = enrolledRepository.findByIsArchivedFalse();
        if(enrolledList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No list of enrollees"));
        }
        return ResponseEntity.ok(enrolledList);
    }

    @GetMapping("/{enrolledId}")
    public ResponseEntity<?> getEnrolleeByEnrolledId(@PathVariable Integer enrolledId) {

        Enrolled existingEnrollee = enrolledRepository.findByEnrolledIdAndIsArchivedFalse(enrolledId);
        if(existingEnrollee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no enrollee with enrolledId: " + enrolledId));
        }

        return ResponseEntity.ok(existingEnrollee);
    }

    @GetMapping("/archived")
    public ResponseEntity<?> getArchivedEnrollees() {

        List<Enrolled> archivedEnrollees = enrolledRepository.findByIsArchivedTrue();
        if(archivedEnrollees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There are no archived enrollees"));
        }
        return ResponseEntity.ok(archivedEnrollees);
    }

    @PostMapping
    public ResponseEntity<?> addEnrollee(@RequestBody EnrollmentRequest enrollmentRequest) {

        Integer studentId = enrollmentRequest.getStudentId();
        Integer courseId = enrollmentRequest.getCourseId();
        Students existingStudent = studentsRepository.findByStudentIdAndIsArchivedFalse(studentId);
        if(existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no student with studentId: " + enrollmentRequest.getStudentId()));
        }

        Courses existingCourse = coursesRepository.findByCourseIdAndIsArchivedFalse(courseId);
        if(existingCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no course with courseId: " + enrollmentRequest.getCourseId()));
        }

        Enrolled addEnrollee = new Enrolled(existingStudent, existingCourse);
        enrolledRepository.save(addEnrollee);
        return ResponseEntity.ok(new MessageResponse(
                String.format("Student with studentId: %d has been enrolled to courseId: %d",
                        studentId,
                        courseId)));
    }

    @PutMapping("/{enrolledId}")
    public ResponseEntity<?> changeEnrolledStudentCourse(@RequestBody UpdateEnrolleeCourseRequest updateEnrolleeCourseRequest, @PathVariable Integer enrolledId) {

        Integer courseId = updateEnrolleeCourseRequest.getCourseId();
        Enrolled existingEnrollee = enrolledRepository.findByEnrolledIdAndIsArchivedFalse(enrolledId);
        if(existingEnrollee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no enrollee with enrolledId: " + enrolledId));
        }

        Integer pastCourseId = existingEnrollee.getCourses().getCourseId();

        Courses newCourse = coursesRepository.findByCourseIdAndIsArchivedFalse(courseId);
        if(newCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no course with courseId: " + courseId));
        }
        existingEnrollee.setCourses(newCourse);

        enrolledRepository.save(existingEnrollee);
        return ResponseEntity.ok(new MessageResponse(
                String.format("Enrollee with enrolledId: %d course has been changed to %d from %d",
                        enrolledId,
                        courseId,
                        pastCourseId)));

    }

    @PutMapping("/archive/{enrolledId}")
    public ResponseEntity<?> archiveEnrolled(@PathVariable Integer enrolledId) {
        Enrolled existingEnrollee = enrolledRepository.findByEnrolledIdAndIsArchivedFalse(enrolledId);
        if(existingEnrollee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no enrollee with enrolledId: " + enrolledId));
        }

        existingEnrollee.setIsArchived(Boolean.TRUE);
        enrolledRepository.save(existingEnrollee);
        return ResponseEntity.ok(new MessageResponse(
                String.format("Enrollee with enrolledId: %d has been archived!", enrolledId)));
    }

    @DeleteMapping("/{enrolledId}")
    public ResponseEntity<?> deleteEnrollee(@PathVariable Integer enrolledId) {

        Enrolled archivedEnrollee = enrolledRepository.findByEnrolledIdAndIsArchivedTrue(enrolledId);
        if (archivedEnrollee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no archived enrollee with enrolledId: " + enrolledId));
        }
        enrolledRepository.delete(archivedEnrollee);
        return ResponseEntity.ok(new MessageResponse(
                String.format("Archived enrollee with enrolledId: %d has been deleted!", enrolledId)));
    }
}
