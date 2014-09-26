package com.yymt.spring.impl;

import com.yymt.spring.HelloWorldService;

public class HelloWorldServiceImpl implements HelloWorldService {

	@Override
	public String sayHello() {
		System.out.println("say hello");
		return "Hello World";
	}

}
