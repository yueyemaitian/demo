package com.tmall.buy.socket.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Socket s = new Socket("192.168.1.104", 8789);
		try {
			InputStream ips = s.getInputStream();
			OutputStream ops = s.getOutputStream();
			String greeting = "Hello !";
			byte[] buffer = new byte[1000];
			long i = 0;
			while (true) {
				ops.write(greeting.getBytes());
				if (ips.read(buffer) > 0) {
					i++;
					if (i % 10000 == 0) {
						System.out.println(new String(buffer) + ", i = " + i);
					}
				}
			}

		} finally {
			s.close();
		}
	}
}
