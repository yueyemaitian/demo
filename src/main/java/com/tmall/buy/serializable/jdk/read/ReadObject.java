package com.tmall.buy.serializable.jdk.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.tmall.buy.serializable.DTO;
/**
 * ���л�����û�г��࣬�����л�������������Class�г��࣬���л��ɹ���<br>
 * ���л�û��ĳ�ֶΣ������л�ʱ������Class�д��ֶΣ������ʼ�������л��࣬�򲻻���ó�ʼ�����룻<br>
 * �����л�����ñ������л�����̳���ϵ�д����������һ��ʵ����Serializable�ӿڵ���ĳ���<br>
 * ���л������л�����������л������readObject/writeObject����<br>
 * �����л�ʱ�����л��ٵ��ֶΡ�����ᱻ���ԣ��������<br>
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
