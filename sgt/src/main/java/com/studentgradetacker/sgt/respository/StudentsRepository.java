package com.studentgradetacker.sgt.respository;

import com.studentgradetacker.sgt.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<Students, Integer> {
}
