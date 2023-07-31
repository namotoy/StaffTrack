package com.example.demo.repository;

import java.util.List;

import com.example.demo.entity.Employee;

public interface EmployeeDao {
	List<Employee> findAll();
}
