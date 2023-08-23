package com.example.demo.app;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class EmployeeForm {
	@Digits(integer = 8, fraction = 0, message = "従業員IDは8桁までの数値で入力してください")
	@NotNull  (message = "従業員IDを入力してください")
	private Integer empId;

	@Size(min = 1, max = 20, message = "従業員名は20文字以内で入力してください")
	@NotEmpty (message = "従業員名を入力してください")
	private String empName;

	@Size(min = 1, max = 255, message = "メールアドレスは255文字以内で入力してください")
	@NotEmpty (message = "メールアドレスを入力してください")
	private String email;

	@Past(message = "生年月日は有効な日付を入力してください")
	@NotNull (message = "生年月日を入力してください")
	private LocalDate birthDate;

	@Digits(integer = 9, fraction = 0, message = "給与は9桁までで入力してください")
	@NotNull (message = "給与を入力してください")
	private Integer salary;

	@NotEmpty (message = "部署を選択してください")
	private String deptName;

	@Size(min = 1, max = 10, message = "パスワードは10文字以内で入力してください")
	@NotEmpty (message = "パスワードを入力してください")
	private String password;

	@NotEmpty (message = "パスワード(確認)を入力してください")
	private String confirmPassword;

	@AssertTrue(message = "パスワードとパスワード(確認)が一致しません")
	public boolean isPasswordValid() {
		//パスワード入力欄がnullや空のときは確認しない
		if (password == null || password.isEmpty()) {
			return true;
		}
		//パスワードの入力内容とパスワード(確認用)の入力内容が等しいか比較
		return password.equals(confirmPassword);
	} 

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
