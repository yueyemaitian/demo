package com.tmall.buy.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFile {

	public static void main(String[] args) throws IOException {
		FileInputStream ips = new FileInputStream("dto.data");
		FileChannel ipsChannel = ips.getChannel();
		FileOutputStream ops = new FileOutputStream("dto.bak");
		FileChannel opsChannel = ops.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(2048);
		while(ipsChannel.read(buffer) > 0){
			buffer.flip();
			opsChannel.write(buffer);
			buffer.clear();
		}
		ips.close();
		ops.close();
		
	}

}
