package com.example.demo.app;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {
	
	private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
	 // メニュー画面への遷移
    @GetMapping("/menu")
    public String showMenu(Model model, HttpSession session) {
        // セッションからユーザー名を取得してモデルに追加
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "menu";
    }
    @GetMapping("/emp_list")
    public String showEmpList(Model model) {
        //従業員リストを取得
		List<Employee> list = employeeService.findAll();
		
		model.addAttribute("list", list);
		model.addAttribute("title", "従業員一覧");
		return "emp_list";
	}
    
    
}
