package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;


@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeServiceImplの単体テスト")
public class EmployeeServiceImplUnitTest {
	@Mock 
	private EmployeeDao dao;
	private Validator validator;
	
	 @InjectMocks
	private EmployeeServiceImpl employeeServiceImpl;
	 
	 @Test
	 @DisplayName("ログイン画面でIDが入力値が空の場合のテスト")
	 void testGetIdNullThrowException() {
		 Employee employee = new Employee();
	     employee.setEmpId(null);
	     
	    Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertFalse(violations.isEmpty());
	 }	 
}
