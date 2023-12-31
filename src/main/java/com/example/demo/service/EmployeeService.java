package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Employee;

import jakarta.servlet.http.HttpSession;

public interface EmployeeService {
	List<Employee> findAll();
	Optional<Employee> userLogin(int id, String password);
	Optional<Employee> findByEmpId(int empId);
	public boolean isEmpIdDuplicated(int empId);
	void insert(Employee employee);
	public void userLogout(HttpSession session);
	Optional<Employee> findById(int empId);
	List<Employee>findByName(String empName);
	public void update(Employee employee);
	public void delete(int empId);
}
