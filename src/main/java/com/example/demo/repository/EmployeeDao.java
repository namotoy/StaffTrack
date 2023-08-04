package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.entity.Employee;

public interface EmployeeDao{
	
	Optional<Employee> findById(int id, String password);
	
}
