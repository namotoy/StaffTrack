package com.example.demo.app;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    HttpSession session;

    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(@RequestParam("id") int id, @RequestParam("password") String password,
                            RedirectAttributes redirectAttributes) {
        Optional<Employee> employee = employeeService.userLogin(id, password);

        if (employee.isPresent()) {
            session.setAttribute("loggedInUser", employee.get());
            return "redirect:/menu";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid ID or password.");
            return "redirect:/login";
        }
    }
}
