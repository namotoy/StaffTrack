package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
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
