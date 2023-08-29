package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

@ExtendWith(MockitoExtension.class)
@DisplayName("従業員削除の単体テスト")
public class EmployeeDeleteUnitTest {
	@Mock 
	private EmployeeDao dao;

	@InjectMocks
	private EmployeeServiceImpl service;

	@Test
	@DisplayName("従業員削除を実行すると、削除が成功するテスト")
	public void testDeleteSuccess() {
		// 任意の従業員IDを設定
		int empId = 10000; 
		// deleteメソッドが呼び出されたときに1を返すようにモックを設定
		when(dao.delete(empId)).thenReturn(1);
		// メソッドの実行
		service.delete(empId);
		// deleteメソッドが正確に1回呼び出されたか確認
		verify(dao, times(1)).delete(empId);
	}

	@Test
	@DisplayName("削除処理の完了後、データベースからも従業員情報が削除されるテスト")
	public void testDeleteDB() {
		// 削除用の従業員情報を作成
		Employee employeed = new Employee();
		employeed.setEmpId(456789);

		// モックの設定
		doNothing().when(dao).insert(any(Employee.class));
		// findByEmpIdを実行すると空のOptionalが返される
		when(dao.findByEmpId(employeed.getEmpId())).thenReturn(Optional.empty()); 
		// deleteが呼び出されたとき、1を返すように設定
		when(dao.delete(employeed.getEmpId())).thenReturn(1); 
		// 従業員情報を登録
		service.insert(employeed);
		// 従業員情報の削除
		service.delete(employeed.getEmpId());
		// DBから同じIDの従業員を取得して、削除されているか確認
		Optional<Employee> optionalEmployee = service.findByEmpId(employeed.getEmpId());
		assertFalse(optionalEmployee.isPresent(), "従業員情報がデータベースから削除されていません");
	}

	@Test
	@DisplayName("削除する従業員IDが取得できない場合、エラーメッセージが表示されるテスト")
	void deleteWhenEmployeeNotFoundThrowsException() {
		// 存在しない従業員IDを設定
		int nonEmployeeId = 9999; 
		// 従業員IDが存在しないとモックする
		when(dao.delete(nonEmployeeId)).thenReturn(0);
		// deleteメソッドがEmployeeNotFoundExceptionをスローすることを検証
		try {
	        service.delete(nonEmployeeId);
	        //例外がスローされない場合はテストが失敗
	        fail("Expected an EmployeeNotFoundException to be thrown");
	    } catch (EmployeeNotFoundException e) {
	        // 予想通り例外がスローされテスト成功
	    }
	}
}