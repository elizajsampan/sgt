package com.studentgradetacker.sgt.respository;

import com.studentgradetacker.sgt.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Integer> {

    List<Students> findAll();

    Students findByStudentId(Integer studentId);



}
