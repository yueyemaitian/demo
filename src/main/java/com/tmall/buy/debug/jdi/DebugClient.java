package com.tmall.buy.debug.jdi;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class DebugClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost",8787);
		byte[] buffer = new byte[100];
		socket.getOutputStream().write("JDWP-Handshake".getBytes());
		socket.getInputStream().read(buffer);
		System.out.println("Response: " + new String(buffer));
		socket.close();
	}

}
