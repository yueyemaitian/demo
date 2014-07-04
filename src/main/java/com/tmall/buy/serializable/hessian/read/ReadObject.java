package com.tmall.buy.serializable.hessian.read;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.AbstractHessianInput;
import com.tmall.buy.serializable.DTO;
/**
 * 如果反序列化时类字段比序列化时类实例字段少，不会出错，忽略；对于继承体系的改动，也是如此<br>
 * 反序列化构造对象实例，直接调用的_unsafe.allocateInstance，分配实例但是不运行构造方法<br>
 * 反序列化时候多出来的字段，不会初始化。<br>
 * 序列化反序列化不会调被序列化对象的readObject/writeObject方法<br>
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
