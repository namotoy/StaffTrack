package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDao;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    public Optional<Employee> findbyUser(int id, String password) {
        return employeeDao.findbyUser(id, password);
    }

    public Optional<Employee> userLogin(int id, String password) {
        Optional<Employee> optionalEmployee = findbyUser(id, password);
        if (!optionalEmployee.isPresent()) {
            System.out.println("No employee found with the provided ID and password.");
        }
        return optionalEmployee;
    }
}
