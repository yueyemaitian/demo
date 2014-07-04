package com.tmall.buy.book.multicore.chapter1;

/**
 * @author tianmai.fh
 * @date 2014-05-21 21:21
 */
public class LockTwo implements Lock {
    private volatile int victim;
    @Override
    public void lock() {
        int curIdx = Integer.valueOf(Thread.currentThread().getName());
        victim = curIdx;
        while(victim == curIdx){}

    }

    @Override
    public void unlock() {
//        victim = 1 - Integer.valueOf(Thread.currentThread().getName());
    }
}
