package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.Employee;

public interface EmployeeService {
	Optional<Employee> userLogin(int id, String password);
}