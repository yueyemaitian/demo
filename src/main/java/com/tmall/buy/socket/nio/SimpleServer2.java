package com.tmall.buy.socket.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleServer2 {

	public static void main(String[] args) throws Exception {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		Selector selector = Selector.open();
		ssc.configureBlocking(false);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		ssc.bind(new InetSocketAddress(8008));
		int count = 0;
		while ((count = selector.select(100)) > -1) {
			if (count == 0) {
				continue;
			}
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iter = keys.iterator();
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				int ops = key.interestOps();
				if((SelectionKey.OP_ACCEPT & ops) == SelectionKey.OP_ACCEPT){
//					key.channel().configureBlocking(false);
					SocketChannel clientSc = ((ServerSocketChannel)key.channel()).accept();
					clientSc.configureBlocking(false);
					clientSc.register(selector, SelectionKey.OP_READ);
				}
				if((ops & SelectionKey.OP_READ) == SelectionKey.OP_READ){ 
					SocketChannel sc = (SocketChannel)key.channel();
					
					ByteBuffer buf = ByteBuffer.allocate(1024);
					if(!sc.isOpen() || !sc.isConnected()){
						sc.close();
					}else{
						int dataCnt = sc.read(buf);
						if (dataCnt > 0) {
							buf.flip();
							System.out.println(new String(buf.array()));
							buf.clear();
							buf.put("I'm Server".getBytes());
							buf.flip();
							sc.write(buf);
							buf.clear();
						}else if(dataCnt == -1){
							sc.close();
						}
					}
					
				}
				iter.remove();
			}

		}
	}
}
