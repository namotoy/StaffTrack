package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

@ExtendWith(MockitoExtension.class)
@DisplayName("従業員検索の単体テスト")
public class EmployeeSearchUnitTest {
	@Mock 
	private EmployeeDao dao;
	
	@Mock
    private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private EmployeeServiceImpl service;
	
	@Test
	@DisplayName("従業員ID検索で合致した従業員情報を取得するテスト")
	public void testFindById_WhenEmployeeExists() {
        Employee testEmployee = new Employee();
        testEmployee.setEmpId(50001);

        when(dao.findById(anyInt())).thenReturn(Optional.of(testEmployee));

        Optional<Employee> result = service.findById(50001);

        assertTrue(result.isPresent());
        assertEquals(testEmployee, result.get());
    }
	
	 @Test
	 @DisplayName("従業員名検索で合致した従業員情報を取得するテスト")
	 public void testFindByName_WhenEmployeesExist() {
		 // テスト用の従業員オブジェクトを作成
		    Employee testEmployee = new Employee();
		    testEmployee.setEmpName("山田太郎");
		    // データベースから情報を取得するダミーの挙動を設定
		    when(dao.findByName("山田太郎")).thenReturn(Arrays.asList(testEmployee));
		    // serviceを使って、"山田太郎" で従業員情報を検索して、結果を"results"に保存
		    List<Employee> results = service.findByName("山田太郎");
		    // "results" が空ではないことを確認
		    assertFalse(results.isEmpty());
		    // "results" の最初の従業員の名前が "山田太郎" であることを確認
		    assertEquals("山田太郎", results.get(0).getEmpName());
		}
	 
	 @Test
	 @DisplayName("従業員ID検索で検索結果が0件だった場合、エラーメッセージが出るテスト")
	 public void testFindById_WhenEmployeeDoesNotExist() {
		 when(dao.findById(anyInt())).thenThrow(EmptyResultDataAccessException.class);
		 Exception exception = assertThrows(EmployeeNotFoundException.class, () -> {
			 service.findById(1);
		 });
		 assertEquals("検索条件に該当する従業員は見つかりません", exception.getMessage());
	 }
	 
	 @Test
	 @DisplayName("従業員名検索で検索結果が0件だった場合、エラーメッセージが出るテスト")
	 public void testFindByName_WhenNotEmployeesExist() {
		 when(dao.findByName("あいう")).thenThrow(EmptyResultDataAccessException.class);
		 Exception exception = assertThrows(EmployeeNotFoundException.class, () -> {
			 service.findByName("あいう");
		 });
		 assertEquals("検索条件に該当する従業員は見つかりません", exception.getMessage());
	 }
 }
