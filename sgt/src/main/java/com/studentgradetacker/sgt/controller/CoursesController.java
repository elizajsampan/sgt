package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.respository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sgt")
public class CoursesController {

    @Autowired
    CoursesRepository coursesRepository;

    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        List<Courses> allCourses = coursesRepository.findAll();

        if (allCourses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Courses not found!"));
        }

        return ResponseEntity.ok(allCourses);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> getCourseByCourseId(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course ID is invalid"));
        }

        // Fetch the course by ID from repository
        Courses existingCourse = coursesRepository.findByCourseId(id);

        if (existingCourse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(existingCourse);
    }

}