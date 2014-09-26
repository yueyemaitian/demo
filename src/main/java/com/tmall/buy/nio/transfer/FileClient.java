package com.tmall.buy.nio.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.tmall.buy.common.CatchExceptionThreadPoolExecutor;

public class FileClient {

	private static ExecutorService workderExec = new CatchExceptionThreadPoolExecutor(1, 2, 10000L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), "worker");

	public static void main(String[] args) throws IOException {
		Selector sel = null;
		SocketChannel[] channels = null;
		try {
			channels = new SocketChannel[] { createSocketChannel(), createSocketChannel() };
			sel = Selector.open();
			for (SocketChannel channel : channels) {
				channel.register(sel, SelectionKey.OP_CONNECT);
				channel.connect(new InetSocketAddress("192.168.1.102", 8008));
			}
			int count = 0;
			while ((count = sel.select(10000)) > -1) {
				if (count == 0) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				Set<SelectionKey> selKeys = sel.selectedKeys();
				Iterator<SelectionKey> iter = selKeys.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					iter.remove();

					if ((SelectionKey.OP_WRITE & key.readyOps()) == SelectionKey.OP_WRITE) {
						transferFile(key);
					} else if ((SelectionKey.OP_CONNECT & key.readyOps()) == SelectionKey.OP_CONNECT) {
						if (key.isConnectable() && ((SocketChannel) key.channel()).finishConnect()) {
							key.interestOps(SelectionKey.OP_WRITE);
							System.out.println("Connected");
						}
					}
				}
			}
		} finally {
			if (sel != null) {
				sel.close();
			}

			for (SocketChannel channel : channels) {
				if (channel != null) {
					channel.close();
				}
			}
		}
	}

	private static void transferFile(SelectionKey key) throws IOException {
		// ��write֮ǰ��Ҫ����insterestOps����Ϊ�գ������������channel
		// write���ݵ�ʱ������һ���߳�ÿ�ζ������channel select����ȥд
		// FIXME:��ʱӦ�ðѹ���ʱ��ע���ȥ�أ�����±�ѭ��ÿ�ζ�д����ȥ��ô�죿
		key.interestOps(0);
		final SocketChannel channel = (SocketChannel) key.channel();
		workderExec.submit(new Runnable() {
			@Override
			public void run() {
				FileInputStream ops = null;
				try {
					File file = new File("/Volumes/HDD/software/Web_Widgets_v1_1.zip");
					ops = new FileInputStream(file);
					FileChannel fileChannel = ops.getChannel();
					// ByteBuffer buf = ByteBuffer.allocateDirect(8);
					ByteBuffer buf = ByteBuffer.allocate(8);
					long restSize = file.length();
					buf.putLong(restSize);
					buf.flip();
					channel.write(buf);
					while (restSize > 0) {
						// FIXME������ں˻�������д���ˣ�����ÿ�ζ�д����ȥ����ô�죿���Կ��ǰ�write�¼�ע����ȥ��������Ҫ������ǰ�Ѿ�д�˶��������ˣ��´μ���д
						System.out.println(Thread.currentThread().getName() + " restSize:" + restSize);
						restSize -= fileChannel.transferTo(0, restSize, channel);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (channel != null) {
						try {
							channel.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (ops != null) {
						try {
							ops.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

		});

	}

	private static SocketChannel createSocketChannel() throws IOException {
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		return sc;
	}

}
