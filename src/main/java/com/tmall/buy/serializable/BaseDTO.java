package com.tmall.buy.serializable;

import java.io.ObjectStreamException;

public class BaseDTO  {
	
//	private static final long serialVersionUID = 19332L;
	public Integer base = 2093;
	
	public BaseDTO(){
		System.out.println("BaseDTO Constructor");
	}
	@SuppressWarnings("unused")
	private void readObjectNoData() throws ObjectStreamException {
		System.out.println("readObjectNoData");
	}

}
