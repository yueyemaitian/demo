package com.tmall.buy.socket.common.message;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class MessageBuilder {
	public static Scanner scanner = new Scanner(System.in);
	public static Message buildChatMessage(String username){
		System.out.println("Friend Username: ");
		String friendUsername = scanner.nextLine();
		System.out.println("Echo Friend: " + friendUsername);
		System.out.println("SayHello: ");
		String sayHello = scanner.nextLine();
		System.out.println("Echo sayHello: " + sayHello);
		return new Message(username, friendUsername, "Hello I am "
				+ username + ": " + sayHello);
	}
	public static Message buildLoginMessage(){
		System.out.println("Login Username: ");
		String username = scanner.nextLine();
		System.out.println("Echo Login: " + username);
		return new Message(String.valueOf(username), null);
	}
	
	public static void writeToServer(OutputStream ops, RawMessage rawMsg)
			throws IOException {
		ops.write(convertInt2Bytes(rawMsg.getLength()));
		ops.write(convertInt2Bytes(rawMsg.getOp()));
		ops.write(convertInt2Bytes(rawMsg.getMessageCount()));
		for (int i = 0; i < rawMsg.getContentSize().length; i++) {
			ops.write(convertInt2Bytes(rawMsg.getContentSize()[i]));
			ops.write(rawMsg.getContents()[i]);
		}
	}

	private static byte[] convertInt2Bytes(int v) {
		byte[] r = new byte[4];
		int mask = 0x00FF;
		for (int i = 0; i < r.length; i++) {
			r[i] = (byte) ((v >> i * 8) & mask);
		}
		return r;
	}


}
