package com.tmall.buy.serializable.jdk.write;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.tmall.buy.serializable.DTO;

public class WriteObject {

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		jdkWriteObject();
	}
	
	public static void jdkWriteObject() throws IOException{
		DTO dto = new DTO();
		dto.a = 100;
		dto.c = 309;
		ObjectOutputStream oops = null;
		try {
			File file = new File("dto.data");
//			System.out.println(file.getAbsolutePath());
			oops = new ObjectOutputStream(new FileOutputStream(file));
			oops.writeObject(dto);
		} finally {
			if (oops != null) {
				oops.close();
			}
		}
	}

}
