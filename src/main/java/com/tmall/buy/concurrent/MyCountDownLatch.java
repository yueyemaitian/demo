package com.tmall.buy.concurrent;

/**
 * @author tianmai.fh
 * @date 2014-04-28 11:46
 */
public class MyCountDownLatch {
    private int latch;

    public MyCountDownLatch(int latch){
        this.latch = latch;
    }
    public int getCount(){
        return latch;
    }
    public void countDown(){
        synchronized(this){
            latch--;
            if(latch == 0){
                notifyAll();
            }
        }


    }
    public void await(){
        synchronized (this){
            if(latch > 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
