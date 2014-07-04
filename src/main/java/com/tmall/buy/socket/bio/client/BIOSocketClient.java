package com.tmall.buy.socket.bio.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tmall.buy.socket.common.message.Message;
import com.tmall.buy.socket.common.message.MessageBuilder;
import com.tmall.buy.socket.common.message.RawMessage;
import com.tmall.buy.socket.common.message.parser.ChatMessageParser;
import com.tmall.buy.socket.common.message.parser.LoginMessageParser;

/**
 * @author tianmai.fh
 * @date 2014-05-15 13:27
 */
public class BIOSocketClient {
	static ExecutorService exec = Executors.newCachedThreadPool();
	

	public static void main(String[] args) throws IOException,
			InterruptedException {
		for (int i = 0; i < 2; i++) {
			exec.submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					try {
						communicateWithServer();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			});
		}
	}


	private static void communicateWithServer() throws IOException {
		Socket socket = new Socket("127.0.0.1", 8887);
		OutputStream ops = null;
		final InputStream ips = socket.getInputStream();
		try {
			ops = socket.getOutputStream();
			receiveMssage(ips);
			// login
			Message msg = MessageBuilder.buildLoginMessage();
			RawMessage rawMsg = LoginMessageParser.instance.deParse(msg);
			MessageBuilder.writeToServer(ops, rawMsg);
			// say hello
//			 System.console().readLine("ÇëÊä³ö£º");
			msg = MessageBuilder.buildChatMessage(msg.getUsername());
			while (true) {
				MessageBuilder.writeToServer(ops, ChatMessageParser.instance.deParse(msg));
			}

		} finally {
			if (ips != null) {
				ips.close();
			}
			if (ops != null) {
				ops.close();
			}
			if (socket != null) {
				socket.close();
			}
			MessageBuilder.scanner.close();
		}
	}


	private static void receiveMssage(final InputStream ips) throws IOException {
		exec.execute(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						byte[] buffer = new byte[2048];
						while (ips.read(buffer) > 0) {
							System.out.println(new String(buffer));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}
}
