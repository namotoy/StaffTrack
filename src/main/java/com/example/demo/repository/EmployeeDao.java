package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Employee;

public interface EmployeeDao{
	List<Employee> findAll();
	Optional<Employee> findbyuser(int id, String password);
	Optional<Employee> findByEmpId(int empId);
	public boolean isEmpIdDuplicated(int empId);
	void insert(Employee employee);
	Optional<Employee> findById(int empId);
	List<Employee>findByName(String empName);
}
