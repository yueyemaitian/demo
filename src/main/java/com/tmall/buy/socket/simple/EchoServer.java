package com.tmall.buy.socket.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(8789);
		try {
			Socket s = ss.accept();
			InputStream ips = s.getInputStream();
			OutputStream ops = s.getOutputStream();
			byte[] buffer = new byte[1000];
			int len = 0;
			int i = 0;
			while ((len = ips.read(buffer)) > 0) {
				i++;
				if (i % 10000 == 0) {
					System.out.println(new String(buffer) + ", i = " + i);
				}
				ops.write(buffer, 0, len);
			}
		} finally {
			ss.close();
		}
	}

}
