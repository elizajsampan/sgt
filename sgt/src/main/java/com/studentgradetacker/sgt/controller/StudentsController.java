package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Students;
import com.studentgradetacker.sgt.respository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sgt")
public class StudentsController {

    @Autowired
    StudentsRepository studentsRepository;

    @GetMapping("/students")
   List<Students> getAllStudents() {
        System.out.println(studentsRepository.findAll());
        return studentsRepository.findAll();
    };

    @GetMapping("/students/{id}")
    Students getStudentByStudentId(@PathVariable Integer id) {
        System.out.println(studentsRepository.findByStudentId(id));
        return studentsRepository.findByStudentId(id);
    }

}
