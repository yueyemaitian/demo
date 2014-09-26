package com.tmall.buy.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SimpleClient {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		for (int i = 0; i < 100; i++) {
			simpleConnect();
		}
	}

	public static void simpleConnect() throws IOException, InterruptedException {
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress("127.0.0.1", 8008));
		while (!channel.finishConnect()) {
			Thread.sleep(100);
		}
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put("Hello World........".getBytes());
		buf.flip();
		channel.write(buf);
		buf.clear();
		while (channel.read(buf) <= 0) {
			Thread.sleep(100);
		}
		buf.flip();
		System.out.println(new String(buf.array()));
		channel.close();
	}

}
