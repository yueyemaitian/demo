package com.tmall.buy.queue;

import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tianmai.fh
 * @date 2014-03-17 14:04
 */
public class QueueCompareTest {
    private static class AccessNode {
        AccessNode(long millis) {
            this.millis = millis;
        }

        private long millis;
        private AtomicLong count = new AtomicLong(0);

        public long getMillis() {
            return millis;
        }

        public long getCount() {
            return count.get();
        }

        public void increment() {
            count.incrementAndGet();
        }

        @Override
		public String toString() {
            return "millis: " + millis + "; count: " + count.get();
        }
    }


    static class AccessNodeHoler {
        private Queue<AccessNode> accessNodesQueue = new ConcurrentLinkedDeque<AccessNode>();
        private int MAX_MILLIS_SECOND_COUNT = 50 * 1000;
        private AtomicReference<AccessNode> curNodeRef = new AtomicReference<AccessNode>();
        private AccessNode lastNode;
        private volatile long firstMillis;

        public AccessNodeHoler() {
            firstMillis = getCurrentMillis();
            lastNode = new AccessNode(firstMillis);
            curNodeRef.set(lastNode);
        }

        private long getCurrentMillis() {
            return System.currentTimeMillis() / 1000 * 1000;
        }

        public void add() {
            long curMillis = this.getCurrentMillis();
            AccessNode curNode = curNodeRef.get();
            if (curMillis > curNode.millis) {
                AccessNode newNode = new AccessNode(curMillis);
                if (curNodeRef.compareAndSet(curNode, newNode)) {
                    lastNode = curNode;
                    this.doAdd(curNode);
                } else {
                    curNode = curNodeRef.get();
                }
            }
            curNode.increment();
        }

        private void doAdd(AccessNode node) {
            long largestMillis = firstMillis + MAX_MILLIS_SECOND_COUNT;
            while (node.getMillis() >= largestMillis) {
                //remove
                AccessNode tmpNode = this.accessNodesQueue.poll();
                largestMillis = tmpNode == null ? curNodeRef.get().getMillis() : tmpNode.getMillis() + MAX_MILLIS_SECOND_COUNT;
            }
            this.resetFirstMillis();
            this.accessNodesQueue.offer(node);
        }

        private void resetFirstMillis() {
            AccessNode first = this.accessNodesQueue.peek();
            firstMillis = first == null ? curNodeRef.get().getMillis() : first.getMillis();
        }

        public long getQps() {
            return lastNode.getCount();
        }

        public String getQpsDetail(long startMillis, int count) {
            StringBuilder sb = new StringBuilder(count * 30);
            Iterator<AccessNode> iter = this.accessNodesQueue.iterator();
            long endMillis = startMillis + (count - 1) * 1000;
            firstMillis = endMillis;
            while (iter.hasNext()) {
                AccessNode node = iter.next();
                if (node.getMillis() >= endMillis) {
                    break;
                }
                iter.remove();
                sb.append(node.getMillis()).append(":").append(node.getCount()).append(";");
            }
            this.resetFirstMillis();
            return sb.toString();
        }

    }

    static class AddRunable implements Runnable {
        int interval;

        public AddRunable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            int interval = this.interval;
            Random rdm = new Random();
            while (true) {
                holder.add();
                try {
                    Thread.sleep(rdm.nextInt(interval));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ;

    static class GetDetailRunnable implements Runnable {
        int interval;

        public GetDetailRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            Random rdm = new Random();
            int interval = this.interval * 5;
            long millis = System.currentTimeMillis() / 1000 * 1000;
            while (true) {
                try {
                    int sleepMillis;
                    while ((sleepMillis = rdm.nextInt(interval)) < this.interval) {
                    }
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int count = (int) ((System.currentTimeMillis() - millis) / 1000);
                System.out.println("Total: " + count + "; QPS detail: " + holder.getQpsDetail(millis, count));
                millis = System.currentTimeMillis() / 1000 * 1000;
            }
        }
    }

    ;

    static class GetQpsRunnable implements Runnable {
        int interval;

        public GetQpsRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            Random rdm = new Random();
            int interval = this.interval * 2;
            long count = 0;
            StringBuilder sb = new StringBuilder(1000);
            while (true) {
                try {
                    Thread.sleep(rdm.nextInt(interval));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                if (count % 30 == 0) {
                    count = 0;
                    System.out.println(sb);
                    sb.delete(0, sb.length());
                }
                sb.append("QPS: ").append(holder.getQps()).append("\t");
            }
        }
    }

    ;
    static final AccessNodeHoler holder = new AccessNodeHoler();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        Random rdm = new Random();
        int seed = 30;
        executor.execute(new AddRunable(rdm.nextInt(seed)));
        executor.execute(new AddRunable(rdm.nextInt(seed)));
        executor.execute(new AddRunable(rdm.nextInt(seed)));
        executor.execute(new AddRunable(rdm.nextInt(seed)));
        executor.execute(new GetDetailRunnable(15 * 1000));
        executor.execute(new GetQpsRunnable(500));
    }
}
