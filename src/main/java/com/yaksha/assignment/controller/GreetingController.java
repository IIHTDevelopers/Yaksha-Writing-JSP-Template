package com.yaksha.assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

	// Display form for user input
	@GetMapping("/")
	public String showForm() {
		return "index";
	}

	// Process the form and display personalized greeting
	@GetMapping("/greet")
	public String greetUser(@RequestParam String name, @RequestParam int age, Model model) {
		String greetingMessage = "Hello, " + name + ". You are " + age + " years old!";
		model.addAttribute("greetingMessage", greetingMessage);
		return "greeting";
	}
}
