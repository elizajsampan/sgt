package com.studentgradetacker.sgt.respository;

import com.studentgradetacker.sgt.model.Enrolled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrolledRepository extends JpaRepository<Enrolled, Integer> {

    List<Enrolled> findByIsArchivedFalse();

    List<Enrolled> findByIsArchivedTrue();

    Enrolled findByEnrolledId(Integer enrolledId);


}
