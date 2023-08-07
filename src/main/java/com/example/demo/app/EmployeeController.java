package com.example.demo.app;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class EmployeeController {
	
	private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
        
	// ログイン画面への遷移
    @GetMapping("/login")
    public String showLogin(Model model) {
    	model.addAttribute("employee", new Employee());
        if(model.containsAttribute("errorMessage")) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }
    
    // ログイン成功時のメニュー画面への遷移
    @PostMapping("/login")
    public String userLogin(
                            @Valid Employee employee,
            				BindingResult result,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
    	if(result.hasErrors()){
            return "/login";
        }
	    try {
	    	Optional<Employee> employee1 = employeeService.userLogin(employee.getEmpId(), employee.getPassword());
	    	if (employee1.isPresent()) {
	    		// ログイン成功、ユーザー名をセッションに保存
	    		session.setAttribute("username", employee1.get().getEmpName());
	    		return "redirect:/menu";  
	    	} 
	    		else {
	    		// IDまたはパスワードが無効な場合、エラーメッセージを表示し再度ログイン画面へリダイレクト
	    		redirectAttributes.addFlashAttribute("errorMessage", "IDまたはパスワードに誤りがあります");
	    		 return "redirect:/login"; 
	    	}
	    } catch(EmployeeNotFoundException e) {
	    	// EmployeeNotFoundExceptionが発生した場合、エラーメッセージを表示し再度ログイン画面へリダイレクト
	    	redirectAttributes.addFlashAttribute("errorMessage", "IDまたはパスワードに誤りがあります");
	    	 return "redirect:/login";
	    }
    }
    
    // メニュー画面への遷移
    @GetMapping("/menu")
    public String showMenu(Model model, HttpSession session) {
        // セッションからユーザー名を取得してモデルに追加
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "menu";
    }
    
    // ログアウトボタン押下すると、メニュー画面からログイン画面へ遷移
    @GetMapping("/logout")
    public String userLogout(HttpSession session, RedirectAttributes attributes) {
    	//userLogoutメソッドを呼び出す
    	employeeService.userLogout(session);
    	
        // ログイン画面にリダイレクト
        return "redirect:/login";
    }
}

