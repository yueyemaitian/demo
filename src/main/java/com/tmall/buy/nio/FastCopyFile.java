package com.tmall.buy.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FastCopyFile {
	private static final String SRC_PATH = "/Users/fanhua/Downloads/apache-tomcat-7.0.54.tar.gz";
	private static final String DEST_PATH = "InstallFiddler.dmg2";
	static int size = 102 * 1024 * 1024;//1M
	static int len = 10;
	public static void main(String[] args) throws IOException {
//		testDirectCopyFile();
		testCopyFile();
		
	}
	
	public static void testDirectCopyFile()throws IOException{
		long start ,end;
		for(int i = 0;i < len;i++){
			directCopyFile();
		}
		start = System.currentTimeMillis();
		directCopyFile();
		end = System.currentTimeMillis();
		System.out.println("Direct Copy Spend(ms): " + (end - start));
	}
	public static void testCopyFile()throws IOException{
		long start ,end;
		for(int i = 0;i < len;i++){
			copyFile();
		}

		start = System.currentTimeMillis();
		copyFile();
		end = System.currentTimeMillis();
		System.out.println("Copy Spend(ms): " + (end - start));
	}
	
	public static void directCopyFile() throws IOException{
		FileOutputStream fops = new FileOutputStream(DEST_PATH);
		FileInputStream fips = new FileInputStream(SRC_PATH);
		FileChannel opsChannel = fops.getChannel();
		FileChannel ipsChannel = fips.getChannel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(size);
		while(ipsChannel.read(buffer) > 0){
			buffer.flip();
			opsChannel.write(buffer);
			buffer.clear();
		}
		fops.close();
		fips.close();
	}
	
	
	public static void copyFile() throws IOException{
		FileOutputStream fops = new FileOutputStream(DEST_PATH);
		FileInputStream fips = new FileInputStream(SRC_PATH);
		FileChannel opsChannel = fops.getChannel();
		FileChannel ipsChannel = fips.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(size);
		while(ipsChannel.read(buffer) > 0){
			buffer.flip();
			opsChannel.write(buffer);
			buffer.clear();
		}
		fops.close();
		fips.close();
	}

}
