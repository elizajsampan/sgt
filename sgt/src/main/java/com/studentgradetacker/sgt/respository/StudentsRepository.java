package com.studentgradetacker.sgt.respository;

import com.studentgradetacker.sgt.dto.StudentGradesDTO;
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

    @Query("SELECT new com.example.StudentGradesDTO(s.firstName, s.lastName, g.prelims, g.midterms, g.finals, g.finalGrade) " +
            "FROM Students s " +
            "JOIN s.enrolled e " +
            "JOIN e.grades g " +
            "WHERE s.studentId = :studentId AND s.isArchived = false AND e.isArchived = false AND g.isArchived = false")
    List<StudentGradesDTO> findStudentGradesByStudentId(@Param("studentId") Integer studentId);


}
