package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.respository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sgt")
public class CoursesController {

    @Autowired
    CoursesRepository coursesRepository;

    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        List<Courses> allCourses = coursesRepository.findByIsArchivedFalse();

        if (allCourses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Courses not found!"));
        }

        return ResponseEntity.ok(allCourses);
    }

    @GetMapping("/courses/archived")
    public ResponseEntity<?> getAllArchivedCourses() {
        List<Courses> allArchivedCourses = coursesRepository.findByIsArchivedTrue();

        if (allArchivedCourses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There are no archived courses"));
        }

        return ResponseEntity.ok(allArchivedCourses);
    }

    @GetMapping("/course/{id}")
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

    @PutMapping("/course/archive/{id}")
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
}