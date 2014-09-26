package com.tmall.buy.socket.nio.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedCopyFile {

	public static void main(String[] args) throws IOException {
		int size = 10 * 1024 * 1024;//10k
//		RandomAccessFile file = ;
//		FileInputStream fips = new FileInputStream(file);
		RandomAccessFile file = new RandomAccessFile("dto.data","r");
		FileChannel fipsChannel = file.getChannel();
		FileOutputStream fops = new FileOutputStream("dto.data4");
		long totalSize = fipsChannel.size();
		long currenSize = 0;
		while(totalSize > currenSize){
			MappedByteBuffer mbp = fipsChannel.map(FileChannel.MapMode.READ_ONLY, currenSize, size);
			currenSize += size;
			fops.getChannel().write(mbp);
		}
		
//		fips.close();
		file.close();
		fops.close();
	}

}
