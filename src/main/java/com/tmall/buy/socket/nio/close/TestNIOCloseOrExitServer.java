package com.tmall.buy.socket.nio.close;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TestNIOCloseOrExitServer {

	public static void main(String[] args) throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress(8008));
		Selector sel = Selector.open();
		ssc.register(sel, SelectionKey.OP_ACCEPT);
		int len = 0;
		while ((len = sel.select(10000)) > -1) {
			if (len == 0) {
				continue;
			}
			Set<SelectionKey> keys = sel.selectedKeys();
			Iterator<SelectionKey> iter = keys.iterator();
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				SelectableChannel ch = key.channel();
				if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					ch = ((ServerSocketChannel) ch).accept();
					ch.configureBlocking(false);
					ch.register(sel, SelectionKey.OP_READ);
					System.out.println(((SocketChannel) ch).getRemoteAddress() + " register op_read");
				}
				if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {

					ByteBuffer dst = ByteBuffer.allocate(1024);
					int rstLen = ch.isOpen() ? ((SocketChannel) ch).read(dst) : -1;
					if (rstLen > 0) {
						dst.flip();
						System.out.println(new String(dst.array()));
					} else {
						key.interestOps(0);
						ch.close();
						System.err.println("closed");
					}
				}
				iter.remove();
			}
		}
	}
}
