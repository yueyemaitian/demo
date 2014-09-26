package com.tmall.buy.socket.nio.close;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TestNIOCloseOrExitClient {

	public static void main(String[] args) throws IOException {
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		sc.connect(new InetSocketAddress(8008));
		Selector sel = Selector.open();
		sc.register(sel, SelectionKey.OP_READ);
		int count = 0;
		while(!sc.finishConnect()){
			if(count++ >= 10){
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(!sc.isConnected()){
			System.out.println("fail to connect");
			return;
		}
		ByteBuffer src = ByteBuffer.allocate(1024);
		src.put("Hello World".getBytes());
		src.flip();
		sc.write(src);
		src.clear();
		int len = 0;
		while((len = sel.select(100000)) > -1){
			if(len == 0){
				continue;
			}
			Set<SelectionKey> keys = sel.selectedKeys();
			Iterator<SelectionKey> iter = keys.iterator();
			while(iter.hasNext()){
				SelectionKey key = iter.next();
				if(SelectionKey.OP_READ == (SelectionKey.OP_READ & key.readyOps())){
					 SocketChannel ch = (SocketChannel)key.channel();
					 if(!ch.isOpen()){
						 key.interestOps(0);
						 ch.close();
						 System.err.println("closed...channel");
					 }
				}
				iter.remove();
			}
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
