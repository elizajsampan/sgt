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

//    @Query("SELECT s.first_name, s.last_name, c.course_name, g.prelims, g.midterms, g.finals\n" +
//            "    FROM grades g left join enrolled e on g.enrolled_id = e.enrolled_id\n" +
//            "    LEFT JOIN courses c on c.course_id = e.course_id\n" +
//            "    left join students s on s.student_id = e.student_id;")
//    List<StudentGradesDTO> findStudentGradesByStudentId(@Param("studentId") Integer studentId);


}
