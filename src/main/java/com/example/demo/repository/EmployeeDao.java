package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Integer> {
    @Query("SELECT e FROM Employee e WHERE e.empId = :id AND e.password = :password")
    Optional<Employee> findbyUser(@Param("id") int id, @Param("password") String password);
}
