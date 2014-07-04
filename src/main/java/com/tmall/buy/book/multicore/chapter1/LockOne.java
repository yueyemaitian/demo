package com.tmall.buy.book.multicore.chapter1;

/**
 * @author tianmai.fh
 * @date 2014-05-21 20:49
 */
public class LockOne implements Lock {

    private volatile boolean[] status = new boolean[2];
    @Override
    public void lock() {
        int curIdx = Integer.valueOf(Thread.currentThread().getName());
        status[curIdx] = true;
        int nextIdx = 1 - curIdx;
        while (status[nextIdx]) {
        }
    }

    @Override
    public void unlock() {
        int curIdx = Integer.valueOf(Thread.currentThread().getName());
        status[curIdx] = false;
    }

}
