package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.Employee;

import jakarta.servlet.http.HttpSession;

public interface EmployeeService {
	Optional<Employee> userLogin(int id, String password);
	public void userLogout(HttpSession session);
}
