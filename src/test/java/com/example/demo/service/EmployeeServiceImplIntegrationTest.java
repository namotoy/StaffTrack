package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.example.demo.entity.Employee;

@SpringJUnitConfig
@SpringBootTest
@DisplayName("EmployeeServiceImplの結合テスト")
public class EmployeeServiceImplIntegrationTest {
	@Autowired
	private EmployeeService employeeService;

	//正常系
//	@Test
//	@DisplayName("ログイン画面でIDとパスワードを入力して、ログインができるテスト")
//	public void userLoginSuccessTest() {
//		try {
//			Optional<Employee> result = employeeService.userLogin(50002, "1111");
//			assertTrue(result.isPresent());
//		} catch (EmployeeNotFoundException e) {
//			fail("テストが失敗");
//		}
//	}
//
//	//ログアウト機能はHttpSessionモックを使うため単体テストのみ
//
//	@Test
//	@DisplayName("従業員一覧リストを取得できた場合のテスト")
//	public void findAllCheckCountTest() {
//		List<Employee> list = employeeService.findAll();
//		assertEquals(4, list.size()); //従業員一覧のリスト数を設定
//	}
//
//	@Test
//	@DisplayName("従業員登録ができた場合のテスト")
//	public void insertSuccessTest() {
//		Employee employee = new Employee();
//		employee.setEmpId(1001); // 重複していないIDを想定
//		employee.setEmpName("山田太郎");
//		employee.setEmail("samplesample@sample.com");
//		employee.setBirthDate(LocalDate.of(2000, 8, 12));
//		employee.setSalary(300000);
//		employee.setDeptName("総務部");
//		employee.setPassword("pass2023");
//
//		try {
//			employeeService.insert(employee);
//		} catch (Exception e) {
//			fail("Exception should not be thrown.");
//		}
//	}
//
//	@Test
//	@DisplayName("従業員IDで検索できるテスト")
//	public void testFindByIdExistingEmployee() {
//		int existingEmpId = 50001; // データベースに存在するIDを想定
//		try {
//			Optional<Employee> employee = employeeService.findById(existingEmpId);
//			assertTrue(employee.isPresent()); // 従業員が存在することを確認
//		} catch (Exception e) {
//			fail("Exception should not be thrown.");
//		}
//	}
//
//	@Test
//	@DisplayName("従業員名で検索できる場合のテスト")
//	public void testFindByNameExistingEmployee() {
//		String existingEmpName = "山田三郎"; // データベースに存在する従業員名
//		try {
//			List<Employee> employees = employeeService.findByName(existingEmpName);
//			assertFalse(employees.isEmpty()); // 従業員が1人以上存在することを確認
//		} catch (Exception e) {
//			fail("Exception should not be thrown.");
//		}
//	}
//
//	@Test
//	@DisplayName("従業員IDで削除ができる場合のテスト")
//	public void testDeleteSuccessfully() {
//		try {
//			employeeService.delete(1003);  // 存在する従業員IDを設定
//			// 例外がスローされなければテスト成功
//		} catch (Exception e) {
//			fail("Unexpected exception type thrown.");
//		}
//	}
//
//	@Test
//	@DisplayName("従業員IDで変更ができる場合のテスト")
//	public void testUpateSuccessfully() {
//		Employee existingEmployee = new Employee();
//		existingEmployee.setEmpId(50003);  // 存在する従業員IDを設定
//		existingEmployee.setEmpName("山田太郎");
//		existingEmployee.setEmail("samplesample@sample.com");
//		existingEmployee.setBirthDate(LocalDate.of(2000, 8, 12));
//		existingEmployee.setSalary(300000);
//		existingEmployee.setDeptName("総務部");
//		existingEmployee.setPassword("pass2023");
//		try {
//			employeeService.update(existingEmployee); 
//			// 例外がスローされなければテスト成功
//		} catch (Exception e) {
//			fail("Unexpected exception type thrown.");
//		}
//	}

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

	@Test
	@DisplayName("従業員一覧リストを取得できない場合のテスト")
	public void testFindAllEmployeesWhenListIsEmpty(){
		List<Employee> list = employeeService.findAll();
		assertTrue(list.isEmpty(), "従業員情報の取得に失敗しました");
	}

	@Test
	@DisplayName("従業員IDが登録できない場合のテスト")
	public void testInsertEmployeeWithDuplicateEmpId() {
		Employee employee = new Employee();
		employee.setEmpId(50001); // すでにデータベースに存在するIDを想定
		employee.setEmpName("山田太郎");
		employee.setEmail("samplesample@sample.com");
		employee.setBirthDate(LocalDate.of(2000, 8, 12));
		employee.setSalary(300000);
		employee.setDeptName("総務部");
		employee.setPassword("pass2023");

		try {
			employeeService.insert(employee);
			fail("Expected DuplicateEmpIdException to be thrown.");
		} catch (DuplicateEmpIdException e) {
			assertEquals("従業員IDが重複しています", e.getMessage());
		} catch (Exception e) {
			fail("Unexpected exception type thrown.");
		}
	}

	@Test
	@DisplayName("従業員IDが存在せず検索できない場合のテスト")
	public void testFindByIdNonExistingEmployee() {
		int nonExistingEmpId = 9999; // データベースに存在しないIDを想定
		try {
			employeeService.findById(nonExistingEmpId);
			fail("Expected EmployeeNotFoundException to be thrown.");
		} catch (EmployeeNotFoundException e) {
			assertEquals("検索条件に該当する従業員は見つかりません", e.getMessage());
		} catch (Exception e) {
			fail("Unexpected exception type thrown.");
		}
	}

	@Test
	@DisplayName("従業員名が存在せず検索できない場合のテスト")
	public void testFindByNameNonExistingEmployee() {
		String nonExistingEmpName = "hoge"; // データベースに存在しない従業員名
		try {
			List<Employee> result = employeeService.findByName(nonExistingEmpName);
			if (result == null || result.isEmpty()) {
				throw new EmployeeNotFoundException("検索条件に該当する従業員は見つかりません");
			}
			fail("Expected EmployeeNotFoundException to be thrown.");
		} catch (EmployeeNotFoundException e) {
			assertEquals("検索条件に該当する従業員は見つかりません", e.getMessage());
		} catch (Exception e) {
			fail("Unexpected exception type thrown.");
		}
	}

	@Test
	@DisplayName("従業員IDが存在せず削除できない場合のテスト")
	public void testDeleteWhenEmployeeDoesNotExist() {
		try {
			employeeService.delete(1);  // 1は存在しない従業員IDと仮定
			fail("Expected EmployeeNotFoundException to be thrown.");
		} catch (EmployeeNotFoundException e) {
			assertEquals("検索条件に該当する従業員は見つかりません", e.getMessage());
		} catch (Exception e) {
			fail("Unexpected exception type thrown.");
		}
	}

	@Test
	@DisplayName("従業員IDが存在せず、変更できない場合のテスト")
	public void testUpdateFailure() {
		Employee nonExistingEmployee = new Employee();
		nonExistingEmployee.setEmpId(99999);  // 存在しない従業員IDを設定
		nonExistingEmployee.setEmpName("山田太郎");
		nonExistingEmployee.setEmail("samplesample@sample.com");
		nonExistingEmployee.setBirthDate(LocalDate.of(2000, 8, 12));
		nonExistingEmployee.setSalary(300000);
		nonExistingEmployee.setDeptName("総務部");
		nonExistingEmployee.setPassword("pass2023");
		try {
			employeeService.update(nonExistingEmployee);
			fail("Expected an EmployeeNotFoundException to be thrown."); 
			// 従業員が存在しないため、更新すると例外がスローされる
		} catch (EmployeeNotFoundException e) {
			// 従業員が存在しないという例外がスローされればテスト成功
			assertEquals("検索条件に該当する従業員は見つかりません", e.getMessage());
		} catch (Exception e) {
			fail("Unexpected exception type thrown: " + e.toString());
		}
	}
}
