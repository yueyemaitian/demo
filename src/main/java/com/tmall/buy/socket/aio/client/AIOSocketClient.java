package com.tmall.buy.socket.aio.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class AIOSocketClient {

	public static void main(String[] args) throws Exception {
		ThreadFactory threadFactory = new ThreadFactory(){
			private AtomicLong threadNumber = new AtomicLong(0);
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(Thread.currentThread().getThreadGroup(), r,"My-AIO-Workder-" + threadNumber.getAndIncrement(),0L);
				if(t.isDaemon()){
					t.setDaemon(false);
				}
				if(t.getPriority() != Thread.NORM_PRIORITY){
					t.setPriority(Thread.NORM_PRIORITY);
				}
				return t;
			}};
		ExecutorService executor = Executors.newCachedThreadPool(threadFactory);
		// AsynchronousSocketGroup
		AsynchronousChannelGroup group = AsynchronousChannelGroup
				.withThreadPool(executor);
		AsynchronousSocketChannel channel = AsynchronousSocketChannel.open(group);
		Future<Void> future = channel.connect(new InetSocketAddress(8008));
		if(future.get() != null){
			System.out.println("Connected!");
		}
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put("Hello, I'm AIO Client!".getBytes());
		buffer.flip();
		channel.write(buffer);
		buffer.clear();
		Future<Integer> bufFuture = channel.read(buffer);
		if(bufFuture.get() > 0){
			buffer.flip();
			System.out.println(new String(buffer.array()));
		}
		channel.close();
		// channel.o
	}

}
