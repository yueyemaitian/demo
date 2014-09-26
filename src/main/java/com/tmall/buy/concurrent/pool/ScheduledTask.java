package com.tmall.buy.concurrent.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledTask {

	public static void main(String[] args) {
//		List<String> list = new ArrayList<String>();
//		((List<Object>)list).add("A");
		ScheduledExecutorService scheduledExec = Executors.newScheduledThreadPool(1);
		
		ScheduledFuture<?> future = scheduledExec.scheduleAtFixedRate(new Runnable() {
			long startSec = System.currentTimeMillis() / 1000;

			@Override
			public void run() {
				long nowSec = System.currentTimeMillis() / 1000;
				System.out.println("Runnable#run after " + (nowSec - startSec) + " second");
				startSec = nowSec;
			}
		}, 5, 10, TimeUnit.SECONDS);
		scheduledExec.scheduleAtFixedRate(new Runnable() {
			long startSec = System.currentTimeMillis() / 1000;

			@Override
			public void run() {
				long nowSec = System.currentTimeMillis() / 1000;
				System.out.println("2Runnable#run after " + (nowSec - startSec) + " second");
				startSec = nowSec;
			}
		}, 5, 10, TimeUnit.SECONDS);
		future.cancel(false);
//		scheduledExec.
		
		System.out.println(-7 % 3);
		AtomicLong al = new AtomicLong(Long.MAX_VALUE);
		System.out.println(al.get() % 3);
		System.out.println(al.addAndGet(2));
		System.out.println(al.get() % 3);
		
		
	}

}
