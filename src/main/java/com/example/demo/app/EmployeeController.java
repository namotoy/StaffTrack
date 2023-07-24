package com.example.demo.app;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {
	
	private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
        
	// ログイン画面への遷移
    @GetMapping("/login")
    public String showLogin(Model model) {
        if(model.containsAttribute("errorMessage")) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }
    
    // ログイン成功時のメニュー画面への遷移
    @PostMapping("/login")
    public String userLogin(@RequestParam("id") Integer id,
                            @RequestParam("password") String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
    
    	Optional<Employee> employee = employeeService.userLogin(id, password);
    	 if (employee.isPresent()) {
    	        // ログイン成功
    	        return "redirect:/menu";  
    	    } else {
    	        // IDまたはパスワードが無効な場合、再度ログイン画面へリダイレクト
    	        redirectAttributes.addFlashAttribute("errorMessage", "Invalid ID or Password");
    	        return "redirect:/login"; 
    	    }
    	
    }
    // メニュー画面への遷移
    @GetMapping("/menu")
    public String showMenu(Model model) {
        return "menu";
    }
}

