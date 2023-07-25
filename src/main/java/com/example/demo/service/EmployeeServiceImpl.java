package com.example.demo.service;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeDao dao;
	public EmployeeServiceImpl(EmployeeDao dao) {
		this.dao = dao;
	}
	
	public Optional<Employee> userLogin(int id, String password) throws EmployeeNotFoundException {
		//データ一件を取得。idとpasswoordが無ければ例外発生　
		try {
			return dao.findById(id, password);
		} catch (EmptyResultDataAccessException e) {
			throw new EmployeeNotFoundException("IDまたはパスワードに誤りがあります");
		}
	}
}
