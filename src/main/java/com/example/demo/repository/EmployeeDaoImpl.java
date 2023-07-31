package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
				+ " FROM Employee "
				+ " INNER JOIN Department ON Employee.dept_id = Department.dept_id";
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
}
