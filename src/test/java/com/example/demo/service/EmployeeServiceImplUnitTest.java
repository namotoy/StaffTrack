package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeServiceImplの単体テスト")

public class EmployeeServiceImplUnitTest {
	@Mock 
	private EmployeeDao dao;
	
	@InjectMocks
	private EmployeeServiceImpl employeeServiceImpl;
	
	@Test
	@DisplayName("DBの従業員情報が0件の場合のテスト")
	void testFindAllReturnEmptyList() {
		//空のEmployeeリストを作成
		List<Employee> list = new ArrayList<Employee>();
		
		//daoクラスのfindAll()メソッドが呼び出された時、listが戻り値として返ってくる指定
		when(dao.findAll()).thenReturn(list);
		
		//実際にemployeeServiceImplのfindAll()メソッドを呼び出し、戻り値を変数actualListに格納
		List<Employee> actualList= employeeServiceImpl.findAll();
		
		//daoのfindAll()メソッドが1回呼び出されたことを検証
		 verify(dao, times(1)).findAll();
		 
		 //実際に返されたlistが0件であることを検証
		 assertEquals(0, actualList.size());
	}
	
	@Test
	@DisplayName("従業員情報の全件取得が3件の場合のテスト")
	void testFindAllGetThreeList() {
		//空のEmployeeリストを作成
		List<Employee> list = new ArrayList<Employee>();
		
		//3件分の従業員データを作成
		Employee employee1 = new Employee();
		Employee employee2 = new Employee();
		Employee employee3 = new Employee();
		
		//従業員データをlistに追加
		list.add(employee1);
		list.add(employee2);
		list.add(employee3);
		
		//daoクラスのfindAll()メソッドが呼び出された時、listが戻り値として返ってくる指定
		when(dao.findAll()).thenReturn(list);
		
		//実際にemployeeServiceImplのfindAll()メソッドを呼び出し、戻り値を変数actualListに格納
		List<Employee> actualList= employeeServiceImpl.findAll();
		
		//daoのfindAll()メソッドが1回呼び出されたことを検証
		 verify(dao, times(1)).findAll();
		 
		 //実際に返されたlistが3件であることを検証
		 assertEquals(3, actualList.size());
	}
}
