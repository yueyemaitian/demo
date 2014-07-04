package com.tmall.buy.debug.jdi;

import java.io.FileOutputStream;
import java.io.IOException;

public class DebugMain {

	public static void main(String[] args) throws IOException {
		String greeting = "Hello World!";
		System.out.println(greeting);
		FileOutputStream fops = new FileOutputStream("Hello.txt");
		fops.write(greeting.getBytes());
		fops.close();
	}

}
