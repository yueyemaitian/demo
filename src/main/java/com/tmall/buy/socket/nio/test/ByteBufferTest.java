package com.tmall.buy.socket.nio.test;

import java.nio.ByteBuffer;

public class ByteBufferTest {

	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		for(byte i = 1;i <= 10;i++){
			buffer.put(i);
		}
		buffer.position(2);
		buffer.limit(8);
//		buffer = buffer.asReadOnlyBuffer();
		ByteBuffer buf2 = buffer.slice();
		for(int i = 0,len = buf2.capacity();i < len;i++){
			buf2.put(i, (byte) (buf2.get() * 7));
		}
		buf2.position(0);
		buf2.limit(buf2.capacity());
		while(buf2.remaining() > 0){
			System.out.println(buf2.get());
		}
		System.out.println("buffer.capacity(): " + buffer.capacity());
		buffer.position(0);
		buffer.limit(buffer.capacity());
		while(buffer.remaining() > 0){
			System.out.println(buffer.get());
		}
	}

}
