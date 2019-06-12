package com.example.demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

	@GetMapping("/hello")
	public String testController(){
		return "Hello worldadfasdfas";
	}
}
