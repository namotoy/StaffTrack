package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Employee;

public interface EmployeeService {
	List<Employee> findAll();
	Optional<Employee> userLogin(int id, String password);
	Optional<Employee> findByEmpId(int empId);
	void insert(Employee employee);
}
