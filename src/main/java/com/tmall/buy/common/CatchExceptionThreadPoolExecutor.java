package com.tmall.buy.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CatchExceptionThreadPoolExecutor extends ThreadPoolExecutor {
	public CatchExceptionThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public CatchExceptionThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	public CatchExceptionThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, final String threaName, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadFactory() {
			AtomicInteger idx = new AtomicInteger();

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threaName + "-" + idx.getAndIncrement());
			}
		}, handler);
	}

	public CatchExceptionThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public CatchExceptionThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, final String threaName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadFactory() {
			AtomicInteger idx = new AtomicInteger();

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threaName  + "-" +  idx.getAndIncrement());
			}
		});
	}

	public CatchExceptionThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if (t != null) {
			t.printStackTrace();
			return;
		}
		if (r instanceof FutureTask<?>) {
			FutureTask<?> ft = (FutureTask<?>) r;
			try {
				ft.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (CancellationException e) {
				e.printStackTrace();
			}
		}
	}

}