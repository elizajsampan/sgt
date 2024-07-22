package com.studentgradetacker.sgt.repository;

import com.studentgradetacker.sgt.model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Integer> {

    List<Courses> findByIsArchivedFalse();

    List<Courses> findByIsArchivedTrue();

    Courses findByCourseIdAndIsArchivedFalse(Integer courseId);

    Courses findByCourseIdAndIsArchivedTrue(Integer courseId);

    Courses findByCourseCode(String courseCode);

    @Query("SELECT c FROM Courses c" +
            " JOIN Enrolled e ON c.courseId = e.courses.courseId" +
            " WHERE e.enrolledId = :enrolledId")
    Courses findByEnrolledId(Integer enrolledId);
}