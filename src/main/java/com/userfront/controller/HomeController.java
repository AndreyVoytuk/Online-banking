package com.userfront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.userfront.domain.User;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String home() {
		return "redirect:/index";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping(value="/signup")
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		
		return "signup";
	}
}
