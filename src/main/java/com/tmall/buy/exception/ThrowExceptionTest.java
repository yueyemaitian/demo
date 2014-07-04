package com.tmall.buy.exception;

import static java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author tianmai.fh
 * @date 2014-04-22 11:37
 */
public class ThrowExceptionTest {
    public static void main(String[] args) throws Exception {
        MyThreadGroup myTg = new MyThreadGroup("main", new MyGroupUncaughtExceptionHandler());
        new Thread(myTg,"mine"){
            @Override
            public void run() {
//                Thread.currentThread().setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
                Thread.setDefaultUncaughtExceptionHandler(new MyDefaultUncaughtExceptionHandler());
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("Exception");
            }
        }.start();

    }

    static class MyDefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.err.println("Default Handler ======Thread: " + t.getName() + "======");
            e.printStackTrace(System.err);
        }
    }


    static class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.err.println("My Thread handler ======Thread: " + t.getName() + "======");
            e.printStackTrace(System.err);
        }
    }

    static class MyGroupUncaughtExceptionHandler implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.err.println("My Group handler ======Thread: " + t.getName() + "======");
            e.printStackTrace(System.err);
        }
    }

    static class MyThreadGroup extends ThreadGroup {
        UncaughtExceptionHandler ueh;
        public MyThreadGroup(String name,UncaughtExceptionHandler ueh) {
            super(name);
            this.ueh = ueh;
        }

        public MyThreadGroup(String name) {
            super(name);
        }

        public MyThreadGroup(ThreadGroup parent, String name) {
            super(parent, name);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
//            if (getParent() != null) {
//                getParent().uncaughtException(t, e);
////            } else {
//                UncaughtExceptionHandler due = Thread.getDefaultUncaughtExceptionHandler();
//                if (due != null) {
//                    due.uncaughtException(t, e);
//                } else if(this.ueh != null) {
                   this.ueh.uncaughtException(t,e);
//                }
            }
//            super.uncaughtException(t, e);
//        }
    }
}
