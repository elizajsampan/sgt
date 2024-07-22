package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.dto.custom_DTO.EnrolledCoursesDTO;
import com.studentgradetacker.sgt.dto.custom_DTO.StudentDetailsDTO;
import com.studentgradetacker.sgt.dto.custom_DTO_mapper.EnrolledCoursesDTOMapper;
import com.studentgradetacker.sgt.dto.custom_DTO_mapper.StudentDetailsDTOMapper;
import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.Enrolled;
import com.studentgradetacker.sgt.model.Students;
import com.studentgradetacker.sgt.model.payload.request.AddEnrollmentRequest;
import com.studentgradetacker.sgt.model.payload.request.UpdateEnrolleeCourseRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.model.payload.response.StudentEnrolledCoursesResponse;
import com.studentgradetacker.sgt.repository.CoursesRepository;
import com.studentgradetacker.sgt.repository.EnrolledRepository;
import com.studentgradetacker.sgt.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    EnrolledCoursesDTOMapper enrolledCoursesDTOMapper;

    @Autowired
    StudentDetailsDTOMapper studentDetailsDTOMapper;

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

    @GetMapping("/courses/{studentId}")
    public ResponseEntity<?> getEnrolledCoursesByStudentId(@PathVariable Integer studentId) {
        if (studentId == null || studentId == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId " + studentId));
        }

        Students existingStudent = studentsRepository.findByStudentIdAndIsArchivedFalse(studentId);
        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("not found student with studentId " + studentId));
        }
        List<Courses> enrolledCourses = enrolledRepository.findEnrolledCoursesByStudentId(studentId);
        if (enrolledCourses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Student with studentId: " + studentId + " is not enrolled!"));
        }
        StudentDetailsDTO studentDetailsDTO = studentDetailsDTOMapper.apply(existingStudent);

        List<EnrolledCoursesDTO> enrolledStudentDTOS = enrolledCourses.stream()
                .map(enrolledCoursesDTOMapper)
                .toList();

        return ResponseEntity.ok(new StudentEnrolledCoursesResponse(studentDetailsDTO, enrolledStudentDTOS));
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
    public ResponseEntity<?> addEnrollee(@RequestBody AddEnrollmentRequest addEnrollmentRequest) {

        Integer studentId = addEnrollmentRequest.getStudentId();
        Integer courseId = addEnrollmentRequest.getCourseId();
        Students existingStudent = studentsRepository.findByStudentIdAndIsArchivedFalse(studentId);
        if(existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no student with studentId: " + addEnrollmentRequest.getStudentId()));
        }

        Courses existingCourse = coursesRepository.findByCourseIdAndIsArchivedFalse(courseId);
        if(existingCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There is no course with courseId: " + addEnrollmentRequest.getCourseId()));
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
