package com.tmall.buy.socket.bio.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.tmall.buy.socket.common.OpHolder;
import com.tmall.buy.socket.common.message.RawMessage;

public class ServerWorker implements Runnable {
	Socket socket;

	public static ConcurrentHashMap<String, Socket> cachedSocket = new ConcurrentHashMap<String, Socket>();
	private byte[] buffer = new byte[2048];

	public ServerWorker(Socket socket) {
		this.socket = socket;
	}

	private int readInt(InputStream ips) throws IOException {
		int value = 0;
		if (ips.read(buffer, 0, 4) > 0) {// ²Ù×÷
			for (int i = 3; i >= 0; i--) {
				value = value << 8 * i | buffer[i];
			}
		}
		return value;
	}

	private byte[] readData(InputStream ips, int len) throws IOException {
		byte[] content = new byte[len];
		int count = 0;
		while ((count += ips.read(content, count, len - count)) < len) {
		}
		return content;

	}

	@Override
	public void run() {
		try {
			InputStream ips = socket.getInputStream();
			while (true) {
				int length = this.readInt(ips);
				int op = this.readInt(ips);
				int messageCount = this.readInt(ips);
				int[] contentSize = new int[messageCount];
				byte[][] contents = new byte[messageCount][];
				for (int i = 0; i < messageCount; i++) {
					contentSize[i] = this.readInt(ips);
					contents[i] = this.readData(ips, contentSize[i]);
				}
				RawMessage rawMsg = new RawMessage(length, op, messageCount,
						contentSize, contents);
				try {
					OpHolder.getActionByOp(rawMsg.getOp()).doAction(rawMsg,
							socket);
				} catch (Exception e) {
					socket.getOutputStream().write(e.getMessage().getBytes());
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}