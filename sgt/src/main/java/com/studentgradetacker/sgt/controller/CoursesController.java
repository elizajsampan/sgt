package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.payload.request.CourseRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.repository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;

@RestController
@RequestMapping("/sgt/courses")
public class CoursesController {

    @Autowired
    CoursesRepository coursesRepository;

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        List<Courses> allCourses = coursesRepository.findByIsArchivedFalse();

        if (allCourses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Courses not found!"));
        }

        return ResponseEntity.ok(allCourses);
    }

    @GetMapping("/archived")
    public ResponseEntity<?> getAllArchivedCourses() {
        List<Courses> allArchivedCourses = coursesRepository.findByIsArchivedTrue();

        if (allArchivedCourses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There are no archived courses"));
        }

        return ResponseEntity.ok(allArchivedCourses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseByCourseId(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course ID is invalid!"));
        }

        // Fetch the course by ID from repository
        Courses existingCourse = coursesRepository.findByCourseIdAndIsArchivedFalse(id);

        if (existingCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course not found!"));
        }

        return ResponseEntity.ok(existingCourse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addCourse")
    public ResponseEntity<?> addCourse(@RequestBody CourseRequest courseRequest) {

        if(courseRequest.getCourseDescription().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Course Description cannot be empty"));
        }
        if(courseRequest.getCourseCode().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Course Code cannot be empty"));
        }
        Courses existingCourse = coursesRepository.findByCourseCode(courseRequest.getCourseCode());
        if (existingCourse != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Course with the given code already exists"));
        }
        if(courseRequest.getUnits() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Units cannot be 0"));
        }

        Courses course = new Courses(courseRequest.getCourseDescription(), courseRequest.getCourseCode(), courseRequest.getUnits());
        coursesRepository.save(course);
        return ResponseEntity.ok(new MessageResponse("Course has been added successfully!"));

    }

    @PutMapping("/{courseId}")
    public ResponseEntity<?> updateCourse(@RequestBody CourseRequest courseRequest, @PathVariable Integer courseId) {

        Courses course = coursesRepository.findByCourseIdAndIsArchivedFalse(courseId);
        if(course.getIsArchived().equals(Boolean.TRUE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("This course is in archive"));
        }
        if (courseRequest.getCourseDescription() != null && !courseRequest.getCourseDescription().isEmpty()) {
            course.setCourseDescription(courseRequest.getCourseDescription());
        }
        if (courseRequest.getCourseCode() != null && !  courseRequest.getCourseCode().isEmpty()) {
            course.setCourseCode(courseRequest.getCourseCode());
        }
        if (courseRequest.getUnits() != null) {
            course.setUnits(courseRequest.getUnits());
        }

        coursesRepository.save(course);
        return ResponseEntity.ok(new MessageResponse("Course has been updated successfully!"));
    }

    @PutMapping("/archive/{id}")
    public ResponseEntity<?> archiveCourse(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course ID is invalid!"));
        }

        Courses existingCourse = coursesRepository.findByCourseIdAndIsArchivedFalse(id);

        if (existingCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course not found!"));
        }

        existingCourse.setIsArchived(Boolean.TRUE);

        coursesRepository.save(existingCourse);

        return ResponseEntity.ok(new MessageResponse("Course has been archived!"));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer courseId) {
        if (courseId == null || courseId <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course ID is invalid!"));
        }

        Courses archivedCourse = coursesRepository.findByCourseIdAndIsArchivedTrue(courseId);

        coursesRepository.delete(archivedCourse);
        return ResponseEntity.ok(new MessageResponse("Archived course has been deleted!"));
    }
}