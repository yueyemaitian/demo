package com.tmall.buy.serializable;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class DTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 998881L;
	public int a = 0;

	public final Integer b = 100;
	public Integer c = 110;
	public final Integer d = 110;
	public final Integer e = 1010;

	public DTO() {
		System.out.println("DTO Constructor");
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		System.out.println("writeObject");
		out.defaultWriteObject();
//		out.write
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		System.out.println("readObject");
		this.c = 200;
//		this.d = 1022;
	}

	@SuppressWarnings("unused")
	private void readObjectNoData() throws ObjectStreamException {
		System.out.println("readObjectNoData");
	}

}
