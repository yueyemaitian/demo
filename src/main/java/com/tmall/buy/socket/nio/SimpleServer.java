package com.tmall.buy.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleServer {

	public static void main(String[] args) throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress(8008));
		Selector selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		int count = 0;
		while ((count = selector.select(100)) > -1) {
			if (count == 0) {
				continue;
			}
			Set<SelectionKey> keySet = selector.selectedKeys();
			Iterator<SelectionKey> iter = keySet.iterator();
			while (iter.hasNext()) {
				SelectionKey tmpKey = iter.next();
				if ((tmpKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					SocketChannel sc = ((ServerSocketChannel) tmpKey.channel()).accept();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
				} else if ((tmpKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
					SocketChannel chal = (SocketChannel) tmpKey.channel();
					ByteBuffer buf = ByteBuffer.allocate(1024);
					int cnt = chal.isOpen() ? chal.read(buf) : -1;
					if(cnt == -1){
						chal.close();
						System.out.println("Close Connection");
					}else{
						buf.flip();
						System.out.println("From Client-" + chal.getRemoteAddress()
								+ ": " + new String(buf.array()));
						ByteBuffer tmpBuf = ByteBuffer.allocate(1024).put("Hello I'm Server".getBytes());
						tmpBuf.flip();
						chal.write(tmpBuf);
					}
				}else{
					System.out.println(tmpKey.readyOps());
				}
				iter.remove();
			}
		}

	}
}
