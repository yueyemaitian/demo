package com.tmall.buy.reflection;
@SuppressWarnings("unused")
public class SubClazz extends SuperClazz {
	public int getValue(){
		return 2;
	}
	private int getValue2(){
		return 2;
	}
	

	private int getValue3(){
		return getValue();
	}
}
