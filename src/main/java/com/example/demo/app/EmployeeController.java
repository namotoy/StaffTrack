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
 // ログイン画面への遷移
    @GetMapping("/login")
    public String showLogin(Model model) {
    	model.addAttribute("employee", new Employee());
        if(model.containsAttribute("errorMessage")) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }
    
	 // メニュー画面への遷移
    @GetMapping("/menu")
    public String showMenu(Model model, HttpSession session) {
        // セッションからユーザー名を取得してモデルに追加
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "menu";
    }
    
    // 従業員一覧画面への遷移
    @GetMapping("/emp_list")
    public String showEmpList(Model model, HttpSession session) {
    	//ログインしていないユーザーが直接従業員一覧ページへアクセスした場合、ログインページへ遷移
//    	User user = (User) session.getAttribute("user");
//        if (user == null) {
//        	return "redirect:/login";
//        }
        
        //従業員リストを取得
		List<Employee> list = employeeService.findAll();
		
		//DBから取得した従業員情報が0件の場合、エラーメッセージを表示
		if (list.isEmpty()) {
		    model.addAttribute("errorMessage", "従業員情報の取得に失敗しました");
		} else {
			model.addAttribute("list", list);
			model.addAttribute("title", "従業員一覧");
		}
		return "emp_list";
	}
    
    
}
