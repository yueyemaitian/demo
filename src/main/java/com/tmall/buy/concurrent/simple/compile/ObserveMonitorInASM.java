package com.tmall.buy.concurrent.simple.compile;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tianmai.fh
 * @date 2014-05-08 13:15
 */
public class ObserveMonitorInASM {

    private static final int TASK_SIZE = 10;
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(TASK_SIZE + 1);

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < TASK_SIZE; i++) {
            exec.submit(new CounterTask());
        }
        cyclicBarrier.await();

        cyclicBarrier = new CyclicBarrier(TASK_SIZE + 1);
        for (int i = 0; i < TASK_SIZE; i++) {
            exec.submit(new CounterTask());
        }
        cyclicBarrier.await();
        exec.shutdown();
    }

    static class CounterTask implements Runnable {
        private SharedObject sharedObject = SharedObject.getInstance();

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                sharedObject.accessSharedObject();
//                sharedObject.accessSharedObject2();
            }
            System.out.println("Spent (ms): " + (System.currentTimeMillis() - start) + " \tNow: " + sharedObject.accessSharedObject());
        }
    }

    static class SharedObject {
        private static SharedObject sharedObject = new SharedObject();

        private long counter = 0;

        private long counter2 = 0;

        private SharedObject() {

        }

        public static SharedObject getInstance() {
            return sharedObject;
        }

        private Object lock = new Object();

        public long accessSharedObject() {
            synchronized (lock) {
                counter++;
            }
            return counter;
        }
        public synchronized long accessSharedObject2() {
            return ++counter;
        }
    }
}
