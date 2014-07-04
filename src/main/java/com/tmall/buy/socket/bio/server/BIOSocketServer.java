package com.tmall.buy.socket.bio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * @author tianmai.fh
 * @date 2014-05-15 13:34
 */
public class BIOSocketServer {
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(8887);
		try {
			while (true) {
				Socket socket = ss.accept();
				Executors.newCachedThreadPool()
						.submit(new ServerWorker(socket));
			}
		} finally {
			ss.close();
		}
	}

}
