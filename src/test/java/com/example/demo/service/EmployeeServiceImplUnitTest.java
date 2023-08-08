package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeServiceImplの単体テスト")
public class EmployeeServiceImplUnitTest {
	@Mock 
	private EmployeeDao dao;
	
	@Mock 
	private HttpSession mockSession;
	
	@InjectMocks
	private EmployeeServiceImpl employeeServiceImpl;
	 
	private Validator validator;

	@BeforeEach
	public void setUp() {
	     ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	     validator = factory.getValidator();
	 }
	 
	 @Test
	 @DisplayName("IDが未入力の場合のテスト")
	 public void testEmpIdNotNull() {
	     Employee employee = new Employee();
	     employee.setEmpId(null); // 従業員IDをnullに設定
	     employee.setPassword("password"); // 仮のパスワードを設定

	     Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
	     assertFalse(violations.isEmpty()); // バリデーションエラーが空でないことを確認

	     ConstraintViolation<Employee> violation = violations.iterator().next();
	     assertEquals("従業員IDは必須です", violation.getMessage()); // エラーメッセージが期待どおりであることを確認
	 }
	 
	 @Test
	 @DisplayName("パスワードが未入力の場合のテスト")
	 public void testPassWordNotNull() {
		 Employee employee = new Employee();
		 employee.setEmpId(12345); //仮の従業員IDを設定
		 employee.setPassword(""); //パスワードを空文字に設定
		 
		 Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
		 assertFalse(violations.isEmpty()); // バリデーションエラーが空でないことを確認
		 
		 ConstraintViolation<Employee> violation = violations.iterator().next();
		 assertEquals("パスワードは必須です", violation.getMessage()); // エラーメッセージが期待どおりであることを確認
	 }
	 
	 @Test
	 @DisplayName("IDまたはパスワードが取得できなかった場合のテスト")
	 void testGetIdPWThrowException() {
		 int id = 12345;  // 仮のIDを設定
		 String password = "password";  //仮のパスワードを設定
		 
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
	 
	 @Test
     @DisplayName("ログアウトを実行した場合、セッションが無効化されるテスト")
    public void userLogoutSessionInvalidated() {
			 // メソッドを実行
	        employeeServiceImpl.userLogout(mockSession);
	
	        // セッションが無効化されるかどうかを検証
	        verify(mockSession, times(1)).invalidate();
		    }
	 
	 @Test
     @DisplayName("セッションが既に無効化されており、ログアウトが失敗するテスト")
	 public void testUserLogoutWithAlreadyInvalidatedSession() {
	        // セッションが無効化された状態を設定
	        doThrow(new IllegalStateException()).when(mockSession).invalidate();
	        
	        // メソッドが呼び出されたときに不正（IllegalStateException）が発生することを検証
	        assertThrows(IllegalStateException.class, () -> {
	        	employeeServiceImpl.userLogout(mockSession);
	        	
        	// 無効化メソッドが1回呼び出されたことを確認
        	verify(mockSession, times(1)).invalidate();
	        });
	    }
	 
	 @Test
	 @DisplayName("ログアウト処理中に例外が発生することを確認するテスト")
	 public void testSessionAlreadyInvalidatedErrorHandling() {
		 	// セッションがすでに無効化されている場合にスローされる例外を設定
		    doThrow(new IllegalStateException("セッションがすでに無効化されています")).when(mockSession).invalidate();

		    // ログアウトで発生する例外を受け取る
		    Exception exception = assertThrows(IllegalStateException.class, () -> {
		        employeeServiceImpl.userLogout(mockSession);
		    });

		    // 発生する例外メッセージと設定した例外メッセージが同一になるか検証
		    assertEquals("セッションがすでに無効化されています", exception.getMessage());
		}
 }
