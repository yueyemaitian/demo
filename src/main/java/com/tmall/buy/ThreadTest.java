package com.tmall.buy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

	public static void main(String[] args) {
		ExecutorService executors = Executors.newFixedThreadPool(2);
		for (int i = 0; i < 100; i++) {
			executors.submit(new Runnable() {
				public void run() {
					System.out.println(Thread.currentThread());
					throw new RuntimeException("");
				}

			});
		}
	}

}
