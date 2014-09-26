package com.tmall.buy.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SimpleClient2 {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		SocketChannel channel = SocketChannel.open();
		Selector selector = Selector.open();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_CONNECT);
		channel.connect(new InetSocketAddress(8008));
		while (!channel.finishConnect()) {
			Thread.sleep(20);
		}
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		for (int i = 0; i < 10; i++) {
			buffer.put("Hello World!".getBytes());
			buffer.flip();
			channel.write(buffer);
			buffer.clear();

			while (channel.read(buffer) <= 0) {
				Thread.sleep(20);
			}
			buffer.flip();
			System.out.println(new String(buffer.array()));
			buffer.clear();
		}
		channel.close();
	}
}
