package com.studentgradetacker.sgt.respository;

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

    Enrolled findByEnrolledId(Integer enrolledId);

//    @Query("SELECT s.studentId FROM Students s" +
//            " JOIN Enrolled e ON s = e.students" +
//            " WHERE s.studentId = :studentId")
//    Enrolled findEnrolledByStudentId(@Param("studentId") Integer studentId);


}
