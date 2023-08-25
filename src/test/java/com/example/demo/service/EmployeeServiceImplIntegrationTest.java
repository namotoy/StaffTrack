package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@SpringBootTest
@DisplayName("EmployeeServiceImplの結合テスト")
public class EmployeeServiceImplIntegrationTest {

	@Autowired
	private EmployeeService employeeService;

	
	//正常系
	
	
	//異常系
	@Test
	@DisplayName("IDとパスワードが取得できずログインできない場合のテスト")
	void testGetIdPwReturnError(){
		try {
			employeeService.userLogin(1111, "pass2023");
			fail("Expected EmployeeNotFoundException to be thrown");
		} catch(EmployeeNotFoundException e) {
			assertEquals("IDまたはパスワードに誤りがあります", e.getMessage());
		}
	}
}
