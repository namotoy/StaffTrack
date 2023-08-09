package com.example.demo.app;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("employee")
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
		}
		model.addAttribute("list", list);
		model.addAttribute("title", "従業員一覧");
		return "emp_list";
	}
    
    // 従業員登録画面への遷移
    @GetMapping("/emp_regist")
    public String showEmpRegist(@ModelAttribute("employeeForm") EmployeeForm employeeForm, Model model) {
    	model.addAttribute("employeeForm", new EmployeeForm());
        return "emp_regist";
    }
    
    // 従業員登録画面から従業員登録確認画面への遷移
    @PostMapping("/emp_regist")
    public String insert(@Valid @ModelAttribute EmployeeForm employeeForm,
    		BindingResult result, Model model) {
    	Employee employee = makeEmployee(employeeForm); 
    	if(result.hasErrors()) {
    		return "emp_regist";  // エラーがある場合、登録画面に戻る
    	}
    	employeeService.insert(employee);
    	model.addAttribute("employee", employeeForm);
    	return "emp_regist_confirm"; // エラーがない場合、確認画面に進む
    }
    
    //入力フォームから送信された情報を従業員リストに追加
    private Employee makeEmployee(EmployeeForm employeeForm) {
    	Employee employee = new Employee();
    	employee.setEmpId(employeeForm.getEmpId());
    	employee.setEmpName(employeeForm.getEmpName());
    	employee.setEmail(employeeForm.getEmail());
    	employee.setBirthDate(employeeForm.getBirthDate());
    	employee.setSalary(employeeForm.getSalary());
    	employee.setDeptName(employeeForm.getDeptName());
    	employee.setPassword(employeeForm.getPassword());
    	
    	return employee;
    }
    
    
    
    // 従業員登録完了画面への遷移
    @GetMapping("/emp_regist_complete")
    public String showEmpRegistComplete(Model model) {
    	return "emp_regist_complete";
    }
    
}
