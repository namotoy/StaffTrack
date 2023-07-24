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

//	@Override
//	public List<Employee> findAll() {
//		// TODO 自動生成されたメソッド・スタブ
//		return null;
//	}

	@Override
	public Optional<Employee> userLogin(int id, String password) throws EmployeeNotFoundException {
		//Optional<Employee>一件を取得 idが無ければ例外発生　
		try {
			return dao.findById(id, password);
		} catch (EmptyResultDataAccessException e) {
			throw new EmployeeNotFoundException("指定された従業員が存在しません");
		}
	}
}
