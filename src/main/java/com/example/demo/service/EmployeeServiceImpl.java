package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private final EmployeeDao dao;
	public EmployeeServiceImpl(EmployeeDao dao) {
		this.dao = dao;
	}
	@Override
	public List<Employee> findAll() {
	return dao.findAll();
	}
}
