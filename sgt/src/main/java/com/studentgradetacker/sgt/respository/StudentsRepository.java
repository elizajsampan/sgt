package com.studentgradetacker.sgt.respository;

import com.studentgradetacker.sgt.dto.custom_DTO.EnrolledStudentDTO;
import com.studentgradetacker.sgt.dto.custom_DTO.StudentGradesDTO;
import com.studentgradetacker.sgt.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Integer> {

    //find all students
    List<Students> findByIsArchivedFalse();

    //find archived students
    List<Students> findByIsArchivedTrue();

    Students findByStudentId(Integer studentId);

    @Query("SELECT new com.studentgradetacker.sgt.dto.custom_DTO.StudentGradesDTO(" +
            "s.firstName, s.lastName, c.courseDescription, c.courseCode, g.prelims, g.midterms, g.finals, g.finalGrade) " +
            "FROM Students s " +
            "JOIN Enrolled e ON s = e.students " +
            "JOIN Courses c ON e.courses = c " +
            "JOIN Grades g ON e = g.enrolled " +
            "WHERE s.studentId = :studentId")
    List<StudentGradesDTO> findGradesByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT new com.studentgradetacker.sgt.dto.custom_DTO.EnrolledStudentDTO(" +
            "s.studentId, c.courseDescription, c.courseCode, c.units)" +
            " FROM Students s" +
            " JOIN Enrolled e ON s = e.students" +
            " JOIN Courses c ON e.courses = c" +
            " WHERE s.studentId = :studentId")
    List<EnrolledStudentDTO> findCoursesEnrolledByStudentId(@Param("studentId") Integer studentId);


}
