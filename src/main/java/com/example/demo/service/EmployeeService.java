package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Employee;

import jakarta.servlet.http.HttpSession;

public interface EmployeeService {
	List<Employee> findAll();
	Optional<Employee> userLogin(int id, String password);
	public void userLogout(HttpSession session);
}
