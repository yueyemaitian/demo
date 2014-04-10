package com.tmall.buy.queue;

import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author tianmai.fh
 * @date 2014-03-11 20:30
 */
public class QueueTest {

    public static void main(String[] args) {
        queueTest();
    }

    public static void queueTest(){
        Queue<String> queue = new ConcurrentLinkedQueue<String>();
        queue.offer("A");
        queue.poll();
        queue.offer("B");
        queue.offer("C");
        queue.poll();
        queue.offer("D");
        queue.poll();
        queue.poll();
        queue.poll();
        System.out.print("hello");
    }

    public static void iteratorAndRemoveTest(){
        Queue<String> queue = new ConcurrentLinkedQueue<String>();
        queue.add("A");
        queue.add("B");
        queue.add("C");

        Iterator<String> iterator = queue.iterator();
        queue.poll();
        System.out.println(iterator.next());
    }

    public static void dequeueTest(){
        final Deque<String> deque = new ConcurrentLinkedDeque<String>();

//1394593987000
//1363058066000
        System.out.println( System.currentTimeMillis() / 1000 * 1000);
//
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    deque.add("1" + i);
                    deque.add("2" + i);
                    deque.add("3" + i);
                    deque.add("4" + i);
//                    try {
//                        Thread.sleep(90);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    deque.pollFirst();
                    deque.pollLast();
                    deque.pollFirst();
                    deque.pollLast();
                    deque.pollFirst();
                    deque.pollLast();
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 4; i++) {
                    Iterator<String> iter = deque.iterator();
                    while (iter.hasNext()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.print(iter.next() + "\t");
                    }
                    System.out.println();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
