package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VipController {

	
	
	@GetMapping("/club/vip")
	@PreAuthorize("hasRole('VIP')") // 시큐리티에 설정된 보안은 URL 레벨의 보안. 
									// 진정한 보안은 메서드 하나하나까지 세밀하게 제어할수 있어야함.
	public String vipLounge() {
		System.out.println("vip 라운지 접근");
		return "vip-lounge";
	}
	
}
