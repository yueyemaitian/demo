package com.tmall.buy.tomcat.study;

public class MyStandardServer extends MyServer {

	public MyStandardServer(){
		System.out.println("MyStandardServer");
	}
	
	public void setMyService(MyService service){
		System.out.println("setMyService");
	}
	
	public void setMyServer(MyServer myServer){
		System.out.println("setMyServer");
	}
}
