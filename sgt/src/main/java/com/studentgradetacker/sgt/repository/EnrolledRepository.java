package com.studentgradetacker.sgt.repository;

import com.studentgradetacker.sgt.dto.custom_DTO.EnrolledCoursesDTO;
import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.Enrolled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrolledRepository extends JpaRepository<Enrolled, Integer> {

    List<Enrolled> findByIsArchivedFalse();

    List<Enrolled> findByIsArchivedTrue();

    Enrolled findByEnrolledIdAndIsArchivedFalse(Integer enrolledId);


    Enrolled findByEnrolledIdAndIsArchivedTrue(Integer enrolledId);

    List<Enrolled> findByStudentsStudentId(Integer studentId);

    @Query("SELECT c " +
            "FROM Students s " +
            "JOIN Enrolled e ON s = e.students " +
            "JOIN Courses c ON e.courses = c " +
            "WHERE s.studentId = :studentId")
    List<Courses> findEnrolledCoursesByStudentId(@Param("studentId") Integer studentId);


}
