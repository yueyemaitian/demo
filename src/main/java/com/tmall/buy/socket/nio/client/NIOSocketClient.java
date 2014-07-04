package com.tmall.buy.socket.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NIOSocketClient {

	public static void main(String[] args) throws IOException {
//		Selector.open().selectedKeys();
//		SocketChannel.open().bind(local)
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
//		channel.bind(new InetSocketAddress(InetAddress.getLocalHost(),));
		channel.connect(new InetSocketAddress("10.68.101.242",8002));
		Selector selector = Selector.open();
		channel.register(selector,SelectionKey.OP_CONNECT);
	}

}
