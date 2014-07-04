package com.tmall.buy.serializable.jdk.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.tmall.buy.serializable.DTO;
/**
 * 序列化对象没有超类，反序列化对象所依赖的Class有超类，序列化成功；<br>
 * 序列化没有某字段，反序列化时依赖的Class有此字段，如果初始化在序列化类，则不会调用初始化代码；<br>
 * 反序列化会调用被反序列化对象继承体系中从下往上最后一个实现了Serializable接口的类的超类<br>
 * 序列化反序列化不会调被序列化对象的readObject/writeObject方法<br>
 * 反序列化时比序列化少的字段、超类会被忽略，不会出错<br>
 * @author fanhua
 *
 */
public class ReadObject {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		jdkReadObject();
	}

	public static void jdkReadObject() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = null;
		try {
			File file = new File("dto.data");
			System.out.println(file.getAbsolutePath());
			ois = new ObjectInputStream(new FileInputStream(file));
			DTO obj = (DTO) ois.readObject();
			System.out.println("ReadObject a: " + obj.a);
			System.out.println("ReadObject b: " + obj.b);
			System.out.println("ReadObject c: " + obj.c);
//			System.out.println("ReadObject base: " + obj.base);
		} finally {
			ois.close();
		}
	}
}
