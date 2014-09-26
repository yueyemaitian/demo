package com.tmall.buy.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tianmai.fh
 * @date 2014-04-26 19:39
 */
public class CountDownLatchTest {
    static Random rdm = new Random(10000L);
    static ExecutorService exec = Executors.newFixedThreadPool(1);

    static class Context {
        final CountDownLatch cdt;
        final List<Integer> parallelValueList = Collections.synchronizedList(new ArrayList<Integer>());
        AtomicInteger ai = new AtomicInteger(0);

        Context(CountDownLatch cdt) {
            this.cdt = cdt;
        }

        public void addOneValue() {
            this.parallelValueList.add(ai.incrementAndGet());
        }

        public List<Integer> getParallelValueList() {
            return this.parallelValueList;
        }
    }

    static interface RunnableTask extends Runnable{
        public void start();
    }

    static class ParallelWorker implements RunnableTask {
        private final Context ctx;
        private RunnableTask[] followTasks;

        ParallelWorker(Context ctx,RunnableTask[] followTasks) {
            this.ctx = ctx;
            this.followTasks = followTasks;
        }
        @Override
        public void run() {
            ctx.cdt.countDown();
            Math.sin(rdm.nextDouble());
            ctx.addOneValue();
            if(ctx.cdt.getCount() == 0 && followTasks != null && followTasks.length > 0){
               for(RunnableTask task : followTasks){
                   task.start();
               }
            }
        }

        @Override
        public void start() {
            exec.submit(this);
        }
    }

    static class JoinWorker implements RunnableTask {
        private final Context ctx;
        private RunnableTask[] followTasks;

        JoinWorker(Context ctx,RunnableTask[] followTasks) {
            this.ctx = ctx;
            this.followTasks = followTasks;
        }

        @Override
        public void start() {
            exec.submit(this);
        }
        @Override
        public void run() {
            try {
                ctx.cdt.await();
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            int rst = 0;
            for(Integer v : ctx.getParallelValueList()){
                rst += v;
            }
            Math.cos(rdm.nextDouble());
            System.out.println("Result : " + rst);
            if(ctx.cdt.getCount() == 0 && followTasks != null && followTasks.length > 0){
                for(RunnableTask task : followTasks){
                    task.start();
                }
            }

        }
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println("Thread: " + t.getName());
                e.printStackTrace(System.err);
            }
        });
        int size = 3;
        CountDownLatch cdt = new CountDownLatch(size);
        Context ctx = new Context(cdt);
        ParallelWorker[] pws = new ParallelWorker[size];
        RunnableTask[] joinTasks = new RunnableTask[1];
        joinTasks[0] = new JoinWorker(ctx,null);
        for (int i = 0; i < 3; i++) {
            new ParallelWorker(ctx,joinTasks).start();
        }
        exec.shutdown();
    }
}
