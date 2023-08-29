package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
	private final JdbcTemplate jdbcTemplate;

	public EmployeeDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Employee> findAll() {
		String sql = "SELECT Employee.emp_id, Employee.emp_name, Employee.email, Employee.birth_date, Employee.salary, Employee.dept_id, Department.dept_name"
		+ " FROM Employee " + " INNER JOIN Department ON Employee.dept_id = Department.dept_id";
		//従業員一覧をMapのListで取得
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		//return用の空のリストを用意	
		List<Employee> list = new ArrayList<Employee>();
		//resultListの中身を取り出し、resultに格納
		for(Map<String,Object> result : resultList) {
			Employee employee = new Employee();
			employee.setEmpId((int)result.get("emp_id"));
			employee.setEmpName((String)result.get("emp_name"));
			employee.setEmail((String)result.get("email"));
			java.sql.Date birthDate = (java.sql.Date) result.get("birth_date");
			employee.setBirthDate(birthDate.toLocalDate());
			employee.setSalary((int)result.get("salary"));
			employee.setDeptId((int)result.get("dept_id"));
			Department department = new Department();
			department.setDeptId((int)result.get("dept_id"));
			department.setDeptName((String)result.get("dept_name"));
			//EmployeeにDepartmentをセットしてテーブルを結合
			employee.setDepartment(department);
			//Employeeをlistに追加
			list.add(employee);
		}
		return list;
	}

	public Optional<Employee> findbyuser(int id, String password){
		String sql = "SELECT emp_id, emp_name, email, birth_date, salary, dept_id, password"
				+ " FROM Employee WHERE emp_id = ? AND password = ? ";
		Map<String, Object> result = jdbcTemplate.queryForMap(sql, id, password);
		Employee employee = new Employee();
		employee.setEmpId((int)result.get("emp_id"));
		employee.setEmpName((String)result.get("emp_name"));
		employee.setEmail((String)result.get("email"));
		java.sql.Date birthDate = (java.sql.Date) result.get("birth_date");
		employee.setBirthDate(birthDate.toLocalDate());
		employee.setSalary((int)result.get("salary"));
		employee.setDeptId((int)result.get("dept_id"));
		employee.setPassword((String)result.get("password"));
		//employeeをOptionalでラップする
		Optional<Employee> employeeOpt = Optional.ofNullable(employee);
		return employeeOpt;
	}

	// dept_nameからdept_idを取得するメソッド
	
	public int getDeptIdByName(String deptName) {
		String sql = "SELECT dept_id FROM department WHERE dept_name = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, deptName);
	}

	@Override
	public void insert(Employee employee) {
		// dept_nameからdept_idを取得
		int deptId = getDeptIdByName(employee.getDeptName());
		// employeeテーブルにデータを挿入
		jdbcTemplate.update("INSERT INTO employee(emp_Id, emp_name, email, birth_date, salary, dept_id, password) VALUES(?, ?, ?, ?, ?, ?, ?)",
		employee.getEmpId(),employee.getEmpName(),employee.getEmail(), employee.getBirthDate(), employee.getSalary(), deptId, employee.getPassword());
	};

	// データベースにすでに登録されている従業員IDを取得するメソッド
	@Override
	public Optional<Employee> findByEmpId(int empId) {
		try {
			String sql = "SELECT emp_id" + " FROM Employee WHERE emp_id = ?";
			Map<String, Object> result = jdbcTemplate.queryForMap(sql, empId);
			Employee employee = new Employee();
			employee.setEmpId((int) result.get("emp_id"));
			// employeeをOptionalでラップする
			return Optional.ofNullable(employee);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	//従業員IDがすでに存在しているか検証
	public boolean isEmpIdDuplicated(int empId) {
		return findByEmpId(empId).isPresent();
	}
	
	// DBから従業員IDに合致した従業員情報を1件取得
	@Override
	public Optional<Employee> findById(int empId){
		String sql ="SELECT emp_id, emp_name, email, birth_date, salary, Employee.dept_id, password, Department.dept_name"
				+ " FROM Employee INNER JOIN Department ON Employee.dept_id = Department.dept_id"
				+ " WHERE emp_id = ? ";
		Map<String, Object> result = jdbcTemplate.queryForMap(sql, empId);
		Employee employee = new Employee();
		employee.setEmpId((int)result.get("emp_id"));
		employee.setEmpName((String)result.get("emp_name"));
		employee.setEmail((String)result.get("email"));
		java.sql.Date birthDate = (java.sql.Date) result.get("birth_date");
		employee.setBirthDate(birthDate.toLocalDate());
		employee.setSalary((int)result.get("salary"));
		employee.setDeptId((int)result.get("dept_id"));
		employee.setPassword((String)result.get("password"));
		Department department = new Department();
		department.setDeptId((int)result.get("dept_id"));
		department.setDeptName((String)result.get("dept_name"));
		//EmployeeにDepartmentをセットしてテーブルを結合
		employee.setDepartment(department);
		Optional<Employee> searchEmployee = Optional.ofNullable(employee);
		return searchEmployee;
	};
	
	// DBから従業員名に合致した従業員情報を全件取得
	@Override
	public List<Employee>findByName(String empName){
		String sql = "SELECT emp_id, emp_name, email, birth_date, salary, Employee.dept_id, password, Department.dept_name"
				+ " FROM Employee INNER JOIN Department ON Employee.dept_id = Department.dept_id"
				+ " WHERE emp_name = ? ";
		//合致した従業員情報をMapのListで取得
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, empName);
		//return用の空のリストを用意	
		List<Employee> list = new ArrayList<Employee>();
		//resultListの中身を取り出し、resultに格納
		for(Map<String,Object> result : resultList) {
			Employee employee = new Employee();
			employee.setEmpId((int)result.get("emp_id"));
			employee.setEmpName((String)result.get("emp_name"));
			employee.setEmail((String)result.get("email"));
			java.sql.Date birthDate = (java.sql.Date) result.get("birth_date");
			employee.setBirthDate(birthDate.toLocalDate());
			employee.setSalary((int)result.get("salary"));
			employee.setDeptId((int)result.get("dept_id"));
			Department department = new Department();
			department.setDeptId((int)result.get("dept_id"));
			department.setDeptName((String)result.get("dept_name"));
			//EmployeeにDepartmentをセットしてテーブルを結合
			employee.setDepartment(department);
			list.add(employee);
		}
		return list;
	};
	
	public int update(Employee employee){
		// dept_nameからdept_idを取得
		int deptId = getDeptIdByName(employee.getDeptName());
		return jdbcTemplate.update("UPDATE employee SET emp_name = ?, email = ?, birth_date = ?, salary = ?, dept_id = ?, password = ? WHERE emp_id = ?",
				employee.getEmpName(),employee.getEmail(), employee.getBirthDate(), employee.getSalary(), deptId, employee.getPassword(), employee.getEmpId());	
	};
  
  public int delete(int empId) {
		return jdbcTemplate.update("DELETE FROM Employee WHERE emp_id = ?", empId);
	};
}
