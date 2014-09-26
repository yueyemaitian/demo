package com.tmall.buy.concurrent.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class TestCapacity {

	public static void main(String[] args) {
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(100);
		for (int i = 0; i < 103; i++) {
			System.out.println(queue.offer(i));//100+ false
		}
		System.out.println("queue2:");
		BlockingQueue<Integer> queue2 = new SynchronousQueue<Integer>();
		for (int i = 0; i < 103; i++) {
			try {
				queue2.put(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(queue2.offer(i));
		}
	}

}
