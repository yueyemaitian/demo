package com.tmall.buy.hook;

/**
 * @author tianmai.fh
 * @date 2014-04-19 17:52
 */
public class ShutdownHookTest {
    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("running shutdown hook...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("running shutdown hook 2...");
            }
        }));

        new Thread(new Runnable(){

            @Override
            public void run() {
                System.out.println("running");
                throw new RuntimeException("");
            }
        }).start();
//        Thread.sleep(1000);
        System.exit(1);
    }
   ;
}
