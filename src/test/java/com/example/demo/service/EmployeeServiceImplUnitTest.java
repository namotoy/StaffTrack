package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.example.demo.repository.EmployeeDao;


@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeServiceImplの単体テスト")
public class EmployeeServiceImplUnitTest {
	@Mock 
	private EmployeeDao dao;
	
	 @InjectMocks
	private EmployeeServiceImpl employeeServiceImpl;
	 
	 @Test
	 @DisplayName("IDまたはパスワードが取得できなかった場合のテスト")
	 void testGetIdPWThrowException() {
		 int id = 12345;  // 例として設定
		 String password = "password";  // 例として設定
		 
		 //daoクラスのfindByIdメソッドが呼び出された時、少なくとも1つの結果が戻り値として返ってくる指定
		 when(dao.findById(id,password)).thenThrow(new EmptyResultDataAccessException(1));
		 
		//IDとPWが取得できないとEmployeeNotFoundExceptionが発生することを検査
		 try {
			 //IDとPWが取得できた場合にEmployeeNotFoundExceptionは発生しない
			 employeeServiceImpl.userLogin(id, password);
			 fail("Expected EmployeeNotFoundException was not thrown.");
		 } catch (EmployeeNotFoundException e) {
			// Expected exception
		 }
	 }	 
}
