package com.tmall.buy.socket.nio.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.tmall.buy.common.CatchExceptionThreadPoolExecutor;

public class FileServer {

	private static ExecutorService bosExecutor = new CatchExceptionThreadPoolExecutor(1, 1, 10000L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100), "bos");

	private static ExecutorService workerExecutor = new CatchExceptionThreadPoolExecutor(1, 4, 0L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100), "worker");

	public static void main(String[] args) throws Exception {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress(8008));
		Selector selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		startBos(selector);
	}

	public static void startBos(final Selector selector) {
		bosExecutor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					int count = 0;
					while ((count = selector.select(10000)) > -1) {//������>0����Ϊ��ʱ���ص�һ����0
						if (count == 0)
							continue;
						Set<SelectionKey> keySet = selector.selectedKeys();
						if (keySet.size() == 0) {
							continue;
						}
						Iterator<SelectionKey> iter = keySet.iterator();
						while (iter.hasNext()) {
							SelectionKey sk = iter.next();
							iter.remove();
							if (!sk.isValid()) {
								try {
									sk.channel().close();
									System.out.println("close");
								} catch (IOException e) {
									e.printStackTrace();
								}
								continue;
							}
							if ((sk.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
								startWorker(sk);
							} else if ((sk.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
								SocketChannel sc = ((ServerSocketChannel) sk.channel()).accept();
								sc.configureBlocking(false);
								sc.register(selector, SelectionKey.OP_READ);
								System.out.println("Accepted");
							} else {
								System.out.println(sk.readyOps());
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	public static void startWorker(SelectionKey sk) {
		// ��read֮ǰ��Ҫ����insterestOps����Ϊ�գ������ڴ����channel
		// read���ݵ�ʱ������һ���߳�ÿ�ζ������channel select����
		//FIXME: ��ʱ�Ѹ���Ȥ���¼�ע���ȥ�أ�����±�ѭ����߶��������ݣ��ǲ�����call������βע���ȥ��
		sk.interestOps(0);
		final SocketChannel channel = (SocketChannel) sk.channel();
		workerExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				ByteBuffer buf = ByteBuffer.allocate(8);
				int len = 0;
				while (len < 8) {
					len += channel.read(buf);
				}
				buf.flip();
				long restLen, totalLen = buf.getLong();
				restLen = totalLen;
				String filePath = "/tmp/" + Thread.currentThread().getName();
				File file = new File(filePath);
				if(!file.exists()){
					file.mkdir();
				}
				FileOutputStream fops = new FileOutputStream(filePath + "/dest.data");
				FileChannel fc = fops.getChannel();
				while (restLen > 0) {
					//FIXME: ������ﳤʱ�������������ô�죿
					System.out.println("left (bytes): " + restLen);
					restLen -= fc.transferFrom(channel, totalLen - restLen, restLen);
				}
				fops.close();
				return null;
			}

		});
	}

}
