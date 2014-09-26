package com.tmall.buy.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
	public static void main(String[] args) {
		ExecutorService exec = new ThreadPoolExecutor(1, 1, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));// Executors.newSingleThreadExecutor();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		try {
			for (int i = 0; i < 1010; i++) {
				exec.submit(r);
				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
			exec.submit(r);
		}
	}
}
