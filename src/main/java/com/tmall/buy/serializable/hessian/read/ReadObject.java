package com.tmall.buy.serializable.hessian.read;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.AbstractHessianInput;
import com.tmall.buy.serializable.DTO;
/**
 * ��������л�ʱ���ֶα����л�ʱ��ʵ���ֶ��٣�����������ԣ����ڼ̳���ϵ�ĸĶ���Ҳ�����<br>
 * �����л��������ʵ����ֱ�ӵ��õ�_unsafe.allocateInstance������ʵ�����ǲ����й��췽��<br>
 * �����л�ʱ���������ֶΣ������ʼ����<br>
 * ���л������л�����������л������readObject/writeObject����<br>
 * 
 * @author fanhua
 *
 */
public class ReadObject {

	public static void main(String[] args) throws IOException {
		HessianProxyFactory factory = new HessianProxyFactory();
		InputStream ips = new FileInputStream("hessian-dto.data");
		AbstractHessianInput hips = null;
		try {
			hips = factory.getHessian2Input(ips);
			DTO obj = (DTO)hips.readObject();
			System.out.println("ReadObject a: " + obj.a);
			System.out.println("ReadObject b: " + obj.b);
			System.out.println("ReadObject c: " + obj.c);
			System.out.println("ReadObject e: " + obj.e);
			System.out.println("ReadObject base: " + obj.base);
		} finally {
			if (hips != null) {
				hips.close();
			}
		}
	}

}
