package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClubLoginController {

	// required = false : 해당 파라미터가 필수가 아니다를 표기.
	//  -> 파라미터에 대한 유연한 설계를 위해 추가해봤음.
	@GetMapping("/club/login")
	public String loginPage(
			@RequestParam(value="error", required = false) String error,
			@RequestParam(value="logout", required = false) String logout, 
			Model model) {
		
		System.out.println("로그인 페이지 접근확인.");
        if (error != null) {
            model.addAttribute("error", true);
            System.out.println("로그인 실패");
        }
        
        if (logout != null) {
            model.addAttribute("logout", true);
            System.out.println("로그아웃 완료");
        }
		
		
		
		return "login";
	}
	
}
