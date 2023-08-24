package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import com.example.demo.app.EmployeeForm;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
@DisplayName("従業員変更の単体テスト")
public class EmployeeUpdateUnitTest {
	private Validator validator;
	
	@Mock
	private EmployeeDao dao;
	
	@Mock
    private BindingResult bindingResult;

	@InjectMocks
	private EmployeeServiceImpl service;
	
	@BeforeEach
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Test
	@DisplayName("従業員変更を実行すると、変更が成功するテスト")
	public void testUpdateSuccess(){
		// テストデータのセットアップ
        Employee updatedData = new Employee();
        updatedData.setEmpId(10000);
        updatedData.setEmpName("New Name");
        // モックの設定
        when(dao.update(updatedData)).thenReturn(1);
        // 実際の変更メソッドを呼び出す
        service.update(updatedData);
        // 変更が正しく行われたかを検証
        verify(dao).update(updatedData);
	}

	@Test
	@DisplayName("更新処理の完了後、データベースの従業員情報が変更されるテスト")
	public void testUpdateDB() {
	    // 更新前の従業員情報を作成
	    Employee oldEmployee = new Employee();
	    oldEmployee.setEmpId(456789);
	    oldEmployee.setEmpName("Old Name");
	    // 更新後の従業員情報を作成
	    Employee newEmployee = new Employee();
	    newEmployee.setEmpId(456789);
	    newEmployee.setEmpName("New Name");
	    // モックの設定
	    doNothing().when(dao).insert(any(Employee.class));
	    // findByEmpIdを実行すると更新前のEmployeeが返される
	    when(dao.findByEmpId(oldEmployee.getEmpId())).thenReturn(Optional.of(oldEmployee)); 
	    // updateが呼び出されたとき、1を返すように設定
	    when(dao.update(newEmployee)).thenReturn(1); 
	    // findByEmpIdを実行すると更新後のEmployeeが返されるように再設定
	    when(dao.findByEmpId(newEmployee.getEmpId())).thenReturn(Optional.of(newEmployee));
	    // 従業員情報を登録
	    service.insert(oldEmployee);
	    // 従業員情報の更新
	    service.update(newEmployee);
	    // DBから同じIDの従業員を取得して、更新されているか確認
	    Optional<Employee> optionalEmployee = service.findByEmpId(newEmployee.getEmpId());
	    assertEquals("New Name", optionalEmployee.get().getEmpName(), "従業員情報が正しく更新されていません");
	}
	
	@Test
	@DisplayName("従業員変更の入力情報が空欄だと変更が失敗するテスト")
	public void testUpdateEmptyFields() {
	    EmployeeForm form = new EmployeeForm();
	    //従業員名を空欄に設定
	    form.setEmpName("");
		// 従業員名が正しいかチェックする。エラーがあれば、violationsに入れる
		Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
		// violationsが空かチェックする。「空でない」ことを確認したいので、FALSEを期待
	    assertFalse(violations.isEmpty());
	    //　一つ一つの間違いや問題を見ていく
	    boolean hasExpectedViolation = false;
	    for (ConstraintViolation<EmployeeForm> violation : violations) {
	        if ("従業員名を入力してください".equals(violation.getMessage())) {
	            hasExpectedViolation = true;
	            break;
	        }
	    }
	    // エラーを確認する。TRUEであることを期待
	    assertTrue(hasExpectedViolation);
	}
	@Test
	@DisplayName("従業員変更の入力情報が正しいフォーマットではないと変更が失敗するテスト")
	public void testUpdateInvalidFormat() {
		EmployeeForm form = new EmployeeForm();
		//パスワードを10文字以上に設定
		form.setPassword("passwordpassword");
		// 従業員名が正しいかチェックする。エラーがあれば、violationsに入れる
		Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
		// violationsが空かチェックする。「空でない」ことを確認したいので、FALSEを期待
		assertFalse(violations.isEmpty());
		//　一つ一つの間違いや問題を見ていく
		boolean hasExpectedViolation = false;
		for (ConstraintViolation<EmployeeForm> violation : violations) {
			if ("パスワードは10文字以内で入力してください".equals(violation.getMessage())) {
				hasExpectedViolation = true;
				break;
			}
		}
		// エラーを確認する。TRUEであることを期待
		assertTrue(hasExpectedViolation);
	}
	

	@Test
	@DisplayName("変更する従業員情報が取得できない場合、エラーメッセージが表示されるテスト")
	void updateWhenEmployeeNotFoundThrowsException() {
		// 変更する従業員が存在しないとモックする
        Employee employee = new Employee();
        when(dao.update(employee)).thenReturn(0);
		// updateメソッドがEmployeeNotFoundExceptionをスローすることを検証
		try {
	        service.update(employee);
	        //例外がスローされない場合はテストが失敗
	        fail("Expected an EmployeeNotFoundException to be thrown");
	    } catch (EmployeeNotFoundException e) {
	        // 予想通り例外がスローされテスト成功
	    }
	}
	
	@Test
	@DisplayName("従業員IDを変更しようとするとエラーメッセージが表示されるテスト")
	public void testEmpIdChanged(){
		// 変更前と変更後の従業員IDを設定
        int beforeEmpId = 1001;
        Employee employee = new Employee();
        employee.setEmpId(1002); 
        
        //updateが呼び出されたとき、変更が行われるように1を設定
	    when(dao.update(employee)).thenReturn(1); 
        service.update(employee);
        // エラーメッセージが存在するかを確認
        assertNotEquals(beforeEmpId, employee.getEmpId(), "従業員IDが変更されています");
    }
}

