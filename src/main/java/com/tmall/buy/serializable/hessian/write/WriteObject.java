package com.tmall.buy.serializable.hessian.write;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.tmall.buy.serializable.DTO;

public class WriteObject {

	public static void main(String[] args) throws IOException {
		HessianProxyFactory factory = new HessianProxyFactory();
		OutputStream ops = new FileOutputStream("hessian-dto.data");
		AbstractHessianOutput hops = null;
		try {
			factory.setHessian2Request(true);
			hops = factory.getHessianOutput(ops);
			DTO dto = new DTO();
			dto.a = 202;
			hops.writeObject(dto);
		} finally {
			if (hops != null) {
				hops.close();
			}
		}
	}

}
