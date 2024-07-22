package com.studentgradetacker.sgt.repository;

import com.studentgradetacker.sgt.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    List<Users> findByIsArchivedFalse();

    List<Users> findByIsArchivedTrue();

    Users findByUserId(Integer userId);

    Users findByUserName(String userName);
}