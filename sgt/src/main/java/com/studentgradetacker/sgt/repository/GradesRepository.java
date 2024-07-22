package com.studentgradetacker.sgt.repository;

import com.studentgradetacker.sgt.model.Enrolled;
import com.studentgradetacker.sgt.model.Grades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradesRepository extends JpaRepository<Grades, Integer> {

    List<Grades> findByIsArchivedFalse();

    List<Grades> findByIsArchivedTrue();

    Grades findByGradeIdAndIsArchivedFalse(Integer gradeId);

    Grades findByGradeIdAndIsArchivedTrue(Integer gradeId);

    Grades findByEnrolledAndIsArchivedFalse(Enrolled enrolled);

    @Query("SELECT g FROM Grades g JOIN g.enrolled e" +
            " WHERE e.students.studentId = :studentId")
    List<Grades> findByStudentId(Integer studentId);

}
