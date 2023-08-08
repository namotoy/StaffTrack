package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Employee;

public interface EmployeeDao{
	List<Employee> findAll();
	Optional<Employee> findById(int id, String password);
	void insert(Employee employee);
}
