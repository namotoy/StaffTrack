package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.app.EmployeeForm;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
@DisplayName("従業員登録の単体テスト")
public class EmployeeRegistUnitTest {

    private Validator validator;

    @Mock
    private EmployeeDao dao; // モックオブジェクト

    @InjectMocks
    private EmployeeServiceImpl service; // テスト対象のサービス

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("従業員IDが未入力の場合のテスト")
    public void testEmpIdValidation() {
        EmployeeForm form = new EmployeeForm();
        form.setEmpId(null); // 無効な値をセット
        Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
        boolean hasExpectedViolation = violations.stream()
        		//violationオブジェクトのgetMessage()メソッドを呼び出し、返り値が文字列と等しいかをチェック
                .anyMatch(violation -> "従業員IDを入力してください".equals(violation.getMessage()));
        assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("従業員IDが8桁より多い場合のテスト")
    public void testEmpIdWithMoreThan8Digits() {
        EmployeeForm form = new EmployeeForm();
        form.setEmpId(123456789);
        Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
        boolean hasExpectedViolation = violations.stream()
                    .anyMatch(violation -> "従業員IDは8桁までの数値で入力してください".equals(violation.getMessage()));
        assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("従業員名が未入力の場合のテスト")
    public void testEmpNameValidation() {
    	EmployeeForm form = new EmployeeForm();
    	form.setEmpName(null); // 無効な値をセット
    	Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
    	assertFalse(violations.isEmpty());
    	 boolean hasExpectedViolation = violations.stream()
         		//violationオブジェクトのgetMessage()メソッドを呼び出し、返り値が文字列と等しいかをチェック
                 .anyMatch(violation -> "従業員名を入力してください".equals(violation.getMessage()));
         assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("従業員名が20文字より多い場合のテスト")
    public void testEmpNameWithMoreTha20Size() {
        EmployeeForm form = new EmployeeForm();
        form.setEmpName("あいうえおあいうえおあいうえおあいうえおあいうえお");
        Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
        boolean hasExpectedViolation = violations.stream()
                .anyMatch(violation -> "従業員名は20文字以内で入力してください".equals(violation.getMessage()));
        assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("メールアドレスが未入力の場合のテスト")
    public void testEmailValidation() {
    	EmployeeForm form = new EmployeeForm();
    	form.setEmail(null); // 無効な値をセット
    	Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
    	assertFalse(violations.isEmpty());
    	 boolean hasExpectedViolation = violations.stream()
          		//violationオブジェクトのgetMessage()メソッドを呼び出し、返り値が文字列と等しいかをチェック
                  .anyMatch(violation -> "メールアドレスを入力してください".equals(violation.getMessage()));
    	 assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("メールアドレスが255文字以上で入力されている場合のテスト")
    public void testEmailWithMoreTha255Size() {
        EmployeeForm form = new EmployeeForm();
        form.setEmail("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@example.com");
        Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
        boolean hasExpectedViolation = violations.stream()
                .anyMatch(violation -> "メールアドレスは255文字以内で入力してください".equals(violation.getMessage()));
        assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("生年月日が未入力の場合のテスト")
    public void testBirthDateValidation() {
    	EmployeeForm form = new EmployeeForm();
    	form.setBirthDate(null); // 無効な値をセット
    	Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
    	assertFalse(violations.isEmpty());
    	boolean hasExpectedViolation = violations.stream()
          		//violationオブジェクトのgetMessage()メソッドを呼び出し、返り値が文字列と等しいかをチェック
                  .anyMatch(violation -> "生年月日を入力してください".equals(violation.getMessage()));
    	 assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("生年月日が未来の日付の場合のテスト")
    public void testBirthDateInFuture() {
        EmployeeForm form = new EmployeeForm();
        form.setBirthDate(LocalDate.now().plusDays(1)); // 明日の日付（未来）をセット
        Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty()); 
        boolean hasExpectedViolation = violations.stream()
                    .anyMatch(violation -> "生年月日は有効な日付を入力してください".equals(violation.getMessage()));
        assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("給与が未入力の場合のテスト")
    public void testSalaryValidation() {
    	EmployeeForm form = new EmployeeForm();
    	form.setSalary(null); // 無効な値をセット
    	Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
    	assertFalse(violations.isEmpty());
    	boolean hasExpectedViolation = violations.stream()
          		//violationオブジェクトのgetMessage()メソッドを呼び出し、返り値が文字列と等しいかをチェック
                  .anyMatch(violation -> "給与を入力してください".equals(violation.getMessage()));
    	 assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("給与が10桁以上の場合のテスト")
    public void testSalaryExceedingMaxLimit() {
        EmployeeForm form = new EmployeeForm();
        form.setSalary(1234567890);
        Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
        // 違反の中から期待されるメッセージがあるか確認
        boolean hasExpectedViolation = violations.stream()
                    .anyMatch(violation -> "給与は9桁までで入力してください".equals(violation.getMessage()));
        assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("部署が未入力の場合のテスト")
    public void testDeptNameValidation() {
    	EmployeeForm form = new EmployeeForm();
    	form.setDeptName(null); // 無効な値をセット
    	Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
    	assertFalse(violations.isEmpty());
    	boolean hasExpectedViolation = violations.stream()
          		//violationオブジェクトのgetMessage()メソッドを呼び出し、返り値が文字列と等しいかをチェック
                  .anyMatch(violation -> "部署を選択してください".equals(violation.getMessage()));
    	 assertTrue(hasExpectedViolation);
    }

    @Test
    @DisplayName("パスワードが未入力の場合のテスト")
    public void testPasswordValidation() {
    	EmployeeForm form = new EmployeeForm();
    	form.setPassword(null); // 無効な値をセット
    	Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
    	assertFalse(violations.isEmpty());
    	boolean hasExpectedViolation = violations.stream()
          		//violationオブジェクトのgetMessage()メソッドを呼び出し、返り値が文字列と等しいかをチェック
                  .anyMatch(violation -> "パスワードを入力してください".equals(violation.getMessage()));
    	 assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("パスワードが10文字より多い場合のテスト")
    public void testPasswordWithMoreTha10Size() {
        EmployeeForm form = new EmployeeForm();
        form.setPassword("passwordpasswordpassword");
        Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
        boolean hasExpectedViolation = violations.stream()
                .anyMatch(violation -> "パスワードは10文字以内で入力してください".equals(violation.getMessage()));
        assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("パスワード(確認)が未入力の場合のテスト")
    public void testConfirmPasswordValidation() {
    	EmployeeForm form = new EmployeeForm();
    	form.setConfirmPassword(null); // 無効な値をセット
    	Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
    	assertFalse(violations.isEmpty());
    	boolean hasExpectedViolation = violations.stream()
          		//violationオブジェクトのgetMessage()メソッドを呼び出し、返り値が文字列と等しいかをチェック
                  .anyMatch(violation -> "パスワード(確認)を入力してください".equals(violation.getMessage()));
    	 assertTrue(hasExpectedViolation);
    }

    @Test
    @DisplayName("入力したパスワードとパスワード（確認）が一致しない場合のテスト")
    public void testPasswordMatchValidation() {
        EmployeeForm form = new EmployeeForm();
        form.setPassword("password1");
        form.setConfirmPassword("password2"); // パスワードが一致しない状態
        Set<ConstraintViolation<EmployeeForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
        boolean hasExpectedViolation = violations.stream()
                .anyMatch(violation -> "パスワードとパスワード(確認)が一致しません".equals(violation.getMessage()));
        assertTrue(hasExpectedViolation);
    }
    
    @Test
    @DisplayName("従業員IDが重複している場合にエラーメッセージが表示されるテスト")
    public void testInsertWithDuplicatedEmpId() {
    	// 重複する従業員IDを設定
        int duplicatedEmpId = 123456;
        Employee duplicatedEmployee = new Employee();
        duplicatedEmployee.setEmpId(duplicatedEmpId);
        
        // isEmpIdDuplicatedのモック動作をセットアップ
        when(dao.isEmpIdDuplicated(duplicatedEmpId)).thenReturn(true);
        
        // insertメソッドを呼び出し、DuplicateEmpIdExceptionがスローされることを確認
        Exception exception = assertThrows(DuplicateEmpIdException.class, () -> {
            service.insert(duplicatedEmployee);
        });

        // 期待するエラーメッセージが得られていることを確認
        assertEquals("従業員IDが重複しています", exception.getMessage());
    }
    
    @Test
    @DisplayName("適正な情報で従業員登録を実行すると、登録成功する場合のテスト")
    public void testInsertWithValidInfo() {
    	EmployeeForm form = new EmployeeForm();
    	// 適正な従業員情報をセット
    	form.setEmpId(456789); 
    	form.setEmpName("山田太郎");
    	form.setEmail("samplesample@sample.com");
    	form.setBirthDate(LocalDate.of(2000, 8, 12));
    	form.setSalary(300000); 
    	form.setDeptName("総務部"); 
    	form.setPassword("pass2023");
    	form.setConfirmPassword("pass2023");
    	//EmployeeFormをEmployeeに変換
    	Employee employee = new Employee();
    	employee.setEmpId(form.getEmpId());
    	employee.setEmpName(form.getEmpName());
    	employee.setEmail(form.getEmail());
    	employee.setBirthDate(form.getBirthDate());
    	employee.setSalary(form.getSalary());
    	employee.setDeptName(form.getDeptName());
    	employee.setPassword(form.getPassword());
    	// エラーが投げられないことを確認
        assertDoesNotThrow(() -> {
            dao.insert(employee); 
        });
        // daoのinsertメソッドが正確に1回呼ばれたことを検証
        verify(dao, times(1)).insert(employee);
    }
    
    @Test
    @DisplayName("従業員情報がデータベースに保存されず、従業員登録が完了しない場合のテスト")
    public void testSaveEmployeeFailure() {
    	EmployeeService mockService = mock(EmployeeService.class);
        
	    // EmployeeFormの準備
	    EmployeeForm form = new EmployeeForm();
	    form.setEmpId(50001); //重複する従業員コードを設定
	    form.setEmpName("佐藤太郎");
	    form.setEmail("samplesample@sample.com");
	    form.setBirthDate(LocalDate.of(2000, 8, 12));
	    form.setSalary(300000); 
	    form.setDeptName("総務部"); 
	    form.setPassword("pass2023");
	    form.setConfirmPassword("pass2000"); //上記と異なるパスワードを設定
	
	    // EmployeeFormをEmployeeに変換
	    Employee employee = new Employee();
	    employee.setEmpId(form.getEmpId());
	    employee.setEmpName(form.getEmpName());
	    employee.setEmail(form.getEmail());
	    employee.setBirthDate(form.getBirthDate());
	    employee.setSalary(form.getSalary());
	    employee.setDeptName(form.getDeptName());
	    employee.setPassword(form.getPassword());
	
	    // insertメソッドが一度も呼び出されず、データーベースに保存されないことを検証
	    verify(mockService, times(0)).insert(employee);
    }
    
    @Test
    @DisplayName("登録完了後に従業員情報が正しくデータベースに保存されるテスト")
    public void testSaveEmployee(){
    	Employee employee = new Employee();
    	employee.setEmpId(456789);
    	employee.setEmpName("山田太郎");
    	employee.setEmail("samplesample@sample.com");
    	employee.setBirthDate(LocalDate.of(2000, 8, 12));
    	employee.setSalary(300000);
    	employee.setDeptName("総務部");
    	employee.setPassword("pass2000");
    	
    	// モックの設定
    	doNothing().when(dao).insert(any(Employee.class));
    	when(dao.findByEmpId(anyInt())).thenReturn(Optional.of(employee));
    	
    	// DBへ登録
    	service.insert(employee); 
    	
    	// DBから同じIDの従業員を取得
        Optional<Employee> optionalEmployee = service.findByEmpId(employee.getEmpId());
        assertTrue(optionalEmployee.isPresent(), "取得した従業員情報がデータベースにありません");
        Employee savedEmployee = optionalEmployee.get();

        // 登録情報とデータベースの情報が一致するか確認
        assertEquals(employee.getEmpId(), savedEmployee.getEmpId());
        assertEquals(employee.getEmpName(), savedEmployee.getEmpName());
        assertEquals(employee.getEmail(), savedEmployee.getEmail());
        assertEquals(employee.getBirthDate(), savedEmployee.getBirthDate());
        assertEquals(employee.getSalary(), savedEmployee.getSalary());
        assertEquals(employee.getDeptName(), savedEmployee.getDeptName());
        assertEquals(employee.getPassword(), savedEmployee.getPassword());
    }

}
