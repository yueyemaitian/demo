package com.tmall.buy.book.multicore.chapter1;

/**
 * @author tianmai.fh
 * @date 2014-05-21 21:20
 */
public class LockMain {

    private static Lock lock = new LockTwo();
    private static int counter = 0;

    public static void addCounter() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }

    static class CounterTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                addCounter();
            }
            System.out.println("done " + Thread.currentThread().getName() + " counter: " + counter);
        }
    }

    public static void main(String[] args) {


        Thread t1 = new Thread(new CounterTask());
        t1.setName("1");
        t1.start();

        Thread t2 = new Thread(new CounterTask());
        t2.setName("0");
        t2.start();
    }
}
