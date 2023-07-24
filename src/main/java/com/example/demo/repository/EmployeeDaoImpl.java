package com.example.demo.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
	private final JdbcTemplate jdbcTemplate;

	public EmployeeDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
//	public List<Employee> findAll() {
//		return null;
//	}
//	
	@Override
	public Optional<Employee> findById(int id, String password){
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
}
	
