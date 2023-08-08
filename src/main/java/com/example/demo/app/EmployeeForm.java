package com.example.demo.app;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class EmployeeForm {
	@Digits(integer = 8, fraction = 0)
	@NotNull (message = "従業員IDを入力してください")
	private Integer empId;
	
	@NotNull (message = "従業員名を入力してください")
	@Size(min = 1, max = 20, message = "20文字以内で入力してください")
    private String empName;
	
	@NotNull (message = "メールアドレスを入力してください")
	@Size(min = 1, max = 255, message = "メールアドレスは255文字以内で入力してください")
    private String email;
	
	@NotNull (message = "生年月日を入力してください")
	@Past(message = "有効な日付を入力してください")
    private LocalDate birthDate;
	
	@NotNull (message = "給与を入力してください")
	@Size(min = 1, max = 255, message = "給与は255文字以内で入力してください")
    private Integer salary;
	
	@NotNull (message = "部署を選択してください")
	private String deptName;
	
	@NotNull (message = "パスワードを入力してください")
	@Size(min = 1, max = 255, message = "パスワードは10文字以内で入力してください")
    private String password;
	
	@NotNull (message = "パスワード(確認)を入力してください")
    private String confirmPassword;
	
}
