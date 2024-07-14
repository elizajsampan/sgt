package com.studentgradetacker.sgt.respository;

import com.studentgradetacker.sgt.model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Integer> {

    List<Courses> findAll();

    Courses findByCourseId(Integer courseId);
}