package com.tmall.buy.reflection;
@SuppressWarnings("unused")
public class SuperClazz {
	
	public int getValue(){
		return 1;
	}
	
	
	private int getValue2(){
		return 1;
	}
	private int getValue3(){
		return getValue();
	}

}
