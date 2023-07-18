package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("")
public class LoginController {
	//ログイン画面への遷移
	@GetMapping("/login")
		 public String getLogin(){ 
	        return "login";
        }
	
	//ログイン成功時のメニュー画面への遷移
//    @PostMapping("/login")
//    String postLogin() {
//        return "redirect:/menu";
//    }
}
