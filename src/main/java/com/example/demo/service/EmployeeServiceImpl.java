package com.example.demo.service;

import java.util.List;
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
	@Override
	public List<Employee> findAll() {
		return dao.findAll();
	}

	public Optional<Employee> userLogin(int id, String password) throws EmployeeNotFoundException {
		//データ1件を取得してidとpasswoordがあるか検証。無ければ例外発生
		try {
			return dao.findById(id, password);
		} catch (EmptyResultDataAccessException e) {
			throw new EmployeeNotFoundException("IDまたはパスワードに誤りがあります");
		}
	}

	//データベースにすでに登録されている従業員IDを取得
	@Override
	public Optional<Employee> findByEmpId(int empId) {
		return dao.findByEmpId(empId);
	}

	//従業員IDがすでに存在しているか検証
	public boolean isEmpIdDuplicated(int empId) {
		return dao.isEmpIdDuplicated(empId);
	}

	@Override
	public void insert(Employee employee) throws DuplicateEmpIdException {
		//すでに存在する従業員IDを登録しようとするとエラー発生
		if (isEmpIdDuplicated(employee.getEmpId())) {
			throw new DuplicateEmpIdException("従業員IDが重複しています");
		}
		//入力情報をデータベースへ保存
		dao.insert(employee);
	}
}
