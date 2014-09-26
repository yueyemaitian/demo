package com.tmall.buy.socket.nio.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
/**
 * 直接读600m的结果（buffer.get一次）：
 * 		Direct Copy Spend(ms): 148
 * 		Copy Spend(ms): 239
 * 		Mapped Copy Spend(ms): 3
 * 
 * @author fanhua
 *
 */
public class FastCopyFile {
	private static final String SRC_PATH = "/Users/fanhua/Downloads/ubuntu-14.04-server-amd64.iso";
	private static final String DEST_PATH = "InstallFiddler.dmg2";
	static int size = 10 * 1024 * 1024;//1M
	static int len = 10;
	public static void main(String[] args) throws IOException {
//		testDirectCopyFile();
//		testCopyFile();
		testCopyMappedFile();
		
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
	
	public static void testCopyMappedFile()throws IOException{
		long start ,end;
		for(int i = 0;i < len;i++){
			mappedCopyFile();
		}

		start = System.currentTimeMillis();
		mappedCopyFile();
		end = System.currentTimeMillis();
		System.out.println("Mapped Copy Spend(ms): " + (end - start));
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
//			buffer.get();
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
//			buffer.get();
			buffer.clear();
		}
		fops.close();
		fips.close();
	}
	
	public static void mappedCopyFile() throws IOException{
		RandomAccessFile raf = new RandomAccessFile(SRC_PATH,"r");
		FileChannel ipsChannel = raf.getChannel();
		RandomAccessFile fops = new RandomAccessFile(DEST_PATH,"rw");
		FileChannel opsChannel = fops.getChannel();
		int totalCount = 0;
		long tmpSize = ipsChannel.size() < size ? ipsChannel.size(): size;
		while(totalCount < ipsChannel.size()){
			MappedByteBuffer mbb = ipsChannel.map(FileChannel.MapMode.READ_ONLY, totalCount,
					ipsChannel.size() - totalCount > tmpSize?tmpSize : ipsChannel.size() - totalCount);
//			mbb.get(mbb.remaining() - 1);
			MappedByteBuffer mbb2 = opsChannel.map(FileChannel.MapMode.READ_WRITE, totalCount,
					ipsChannel.size() - totalCount > tmpSize?tmpSize : ipsChannel.size() - totalCount);
			totalCount += mbb.remaining();
//			opsChannel.write(mbb);
			mbb2.put(mbb);
			mbb2.force();
			mbb.clear();
		}
		fops.close();
		raf.close();
	}

}
