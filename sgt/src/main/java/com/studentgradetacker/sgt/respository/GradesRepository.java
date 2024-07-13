package com.studentgradetacker.sgt.respository;

import com.studentgradetacker.sgt.model.Grades;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradesRepository {

    List<Grades> findAll();

    Grades findByGradeId();

}
