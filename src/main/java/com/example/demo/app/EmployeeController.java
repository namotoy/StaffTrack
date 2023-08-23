package com.example.demo.app;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("employee")
@RequestMapping("/StaffTrack")
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
				return "redirect:/StaffTrack/menu";  
			} 
			else {
				// IDまたはパスワードが無効な場合、エラーメッセージを表示し再度ログイン画面へリダイレクト
				redirectAttributes.addFlashAttribute("errorMessage", "IDまたはパスワードに誤りがあります");
				return "redirect:/StaffTrack/login"; 
			}
		} catch(EmployeeNotFoundException e) {
			// EmployeeNotFoundExceptionが発生した場合、エラーメッセージを表示し再度ログイン画面へリダイレクト
			redirectAttributes.addFlashAttribute("errorMessage", "IDまたはパスワードに誤りがあります");
			return "redirect:/StaffTrack/login";
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

	// 登録画面から確認画面への遷移
	@PostMapping("/emp_regist_confirm")
	public String transitConfirm(@Valid @ModelAttribute EmployeeForm employeeForm,BindingResult result, Model model) {

		// 従業員IDが既に存在するかチェック
		if(employeeForm.getEmpId() != null && employeeService.findByEmpId(employeeForm.getEmpId()).isPresent()) {
			result.rejectValue("empId", "duplicate", "従業員IDが重複しています");
		}
		// エラーがある場合、登録画面に戻る
		if(result.hasErrors()) {
			model.addAttribute("employeForm", employeeForm);
			return "emp_regist"; 
		}
		// エラーがない場合、確認画面に進む
		model.addAttribute("employee", employeeForm);
		return "emp_regist_confirm"; 
	}

	// 確認画面から完了画面への遷移
	@PostMapping("/emp_regist_complete")
	public String insert(@ModelAttribute EmployeeForm employeeForm,
			Model model) {
		// EmployeeFormからEmployeeオブジェクトを作成
		Employee employee = makeEmployee(employeeForm); 
		// データベースに従業員情報を保存
		employeeService.insert(employee);
		return "emp_regist_complete";
	}

	//入力フォームから送信された情報を従業員リストに追加
	public Employee makeEmployee(EmployeeForm employeeForm) {
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
	
	// 従業員検索画面への遷移
		@GetMapping("/emp_search")
		public String showEmpSearch(Employee employee, Model model) {
			return "emp_search";
		}
	// 従業員検索画面での検索結果への遷移
		@PostMapping("/emp_search")
		public String searchEmployee(@RequestParam(required = false) Integer empId, 
		                             @RequestParam(required = false) String empName, 
		                             Model model) {
			// ユーザーがIDで検索した場合
			if (empId != null) {
				Optional<Employee> searchEmpId = employeeService.findById(empId);

				// 該当する従業員情報を画面に表示する
				if (searchEmpId.isPresent()) {
					Employee searchEmployee = searchEmpId.get();       
					model.addAttribute("searchEmployee", searchEmployee);
					return "emp_search";
				} 
			} 
			// ユーザーが名前で検索した場合
			if (empName != null) {
				List<Employee> list = employeeService.findByName(empName);
				// 該当する従業員情報を画面に表示する
				if (!list.isEmpty()) {
					model.addAttribute("list", list);
					return "emp_search";
				} 
			}
			// 何も検索条件が入力されていない場合、または検索結果が見つからなかった場合
			model.addAttribute("errorMessage", "検索条件に該当する従業員は見つかりません");
			return "emp_search";  
		}

		// 従業員一覧画面での検索結果への遷移
		@PostMapping("/emp_list")
		public String searchEmployeeInList(@RequestParam(required = false) Integer empId, 
		                                   @RequestParam(required = false) String empName, 
		                                   Model model) {
			// ユーザーがIDで検索した場合
			if (empId != null) {
				Optional<Employee> searchEmpId = employeeService.findById(empId);
				// 該当する従業員情報を画面に表示する
				if (searchEmpId.isPresent()) {
					Employee searchEmployee = searchEmpId.get();       
					model.addAttribute("searchEmployee", searchEmployee);
					return "emp_list";
				} 
			} 
			// ユーザーが名前で検索した場合
			if (empName != null) {
				List<Employee> list = employeeService.findByName(empName);
				// 該当する従業員情報を画面に表示する
				if (!list.isEmpty()) {
					model.addAttribute("list", list);
					return "emp_list";
				} 
			}
			// 何も検索条件が入力されていない場合、または検索結果が見つからなかった場合
			model.addAttribute("errorMessage", "検索条件に該当する従業員は見つかりません");
			return "emp_list";  
		}
		
		// 従業員変更画面への遷移
		@GetMapping("/emp_update")
		public String showEmpUpdate(Employee employee, Model model) {
			// 実際の従業員IDのリストをデータベースから取得
			List<Employee> employees = employeeService.findAll();
			model.addAttribute("employees", employees);
			return "emp_update";
		}
		// 従業員変更画面への遷移
		@GetMapping("/emp_update_confirm")
		public String showEmpUpdateConfirm(Employee employee, Model model) {
			// 実際の従業員IDのリストをデータベースから取得
			List<Employee> employees = employeeService.findAll();
			model.addAttribute("employees", employees);
			return "emp_update_confirm";
		}
		
		// 変更画面から変更登録画面への遷移
		@PostMapping("/emp_update_input")
		public String transitUpdateInput(Integer empId, Model model) {
			System.out.println(empId);
			if (empId != null) {
				//従業員IDが指定されている場合
				//選択された従業員IDを元にデータベースから従業員情報を取得
				Optional<Employee> fullEmployeeOpt = employeeService.findById(empId);
				model.addAttribute("employeeForm", fullEmployeeOpt.get());
				return "emp_update_input";
			} else {
				//従業員IDが指定されていない場合
				model.addAttribute("errorMessage", "検索条件に該当する従業員は見つかりません");
				//従業員の一覧を取得してモデルに追加
				List<Employee> employees = employeeService.findAll();
				model.addAttribute("employees", employees);
				return "emp_update"; 
			}
		}

		
		// 変更登録画面から変更確認画面への遷移
		@PostMapping("/emp_update_confirm")
		public String transitUpateConfirm(@Valid @ModelAttribute EmployeeForm employeeForm,BindingResult result, Model model) {
			// 従業員IDが既に存在するかチェック
//			if(employeeForm.getEmpId() != null && employeeService.findByEmpId(employeeForm.getEmpId()).isPresent()) {
//				result.rejectValue("empId", "duplicate", "従業員IDが重複しています");
//			}
			// エラーがある場合、登録画面に戻る
			if(result.hasErrors()) {
				model.addAttribute("employeeForm", employeeForm);
				return "emp_update_input"; 
			} else {
			// エラーがない場合、確認画面に進む
			model.addAttribute("employee", employeeForm);
			return "emp_update_confirm"; 
			}
		}
		
		// 変更確認画面から変更完了画面への遷移
		@PostMapping("/emp_update_complete")
		public String update(EmployeeForm employeeForm, int empId) {
			// データベースの従業員情報を更新
			employeeService.update(makeEmployee(null));
			return "emp_update_complete";
		}
}
