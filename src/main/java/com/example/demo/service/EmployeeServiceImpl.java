package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

import jakarta.servlet.http.HttpSession;

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
			return dao.findbyuser(id, password);
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
	
	public void userLogout(HttpSession session) {
		// セッションがnullでないか確認
	    if (session != null) {
	        // セッションを無効化
	        session.invalidate();
	    }
	};
	
	// findByIdメソッドを呼び出し、検索結果があった場合となかった場合で処理を記述
	@Override
	public Optional<Employee> findById(int empId){
		//データ1件を取得してempIdがあるか検証
		try {
			return dao.findById(empId);
		} catch (EmptyResultDataAccessException e) {
		//無ければ例外発生
			throw new EmployeeNotFoundException("検索条件に該当する従業員は見つかりません");
		}
	};
	
	// findByNameメソッドを呼び出し、検索結果があった場合となかった場合で処理を記述
	@Override
	public List<Employee>findByName(String empName){
		try {
	        return dao.findByName(empName);
		} catch (EmptyResultDataAccessException e) {
			//無ければ例外発生
			throw new EmployeeNotFoundException("検索条件に該当する従業員は見つかりません");
		}
	};
	
	public void update(Employee employee) {
		//従業員IDがなければ例外発生
		if(dao.update(employee)==0) {
			throw new EmployeeNotFoundException("検索条件に該当する従業員は見つかりません");
    }
	}
  
	public void delete(int empId) {
		//従業員情報を削除。従業員IDがなければ例外発生
		if(dao.delete(empId)==0) {
			throw new EmployeeNotFoundException("検索条件に該当する従業員は見つかりません");
		}
	}
}
