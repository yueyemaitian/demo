package com.tmall.buy.concurrent.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author tianmai.fh
 * @date 2014-04-13 21:54
 */
public class BlockingQueueTest {
    static BlockingQueue<String> queue = new MyArrayBlockingQueue<String>(10);
    static Random rdm = new Random();

    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    static class Producer implements Runnable {

        @Override
        public void run() {
            int i = 0;
            while (true) {
                try {
                    long start = System.currentTimeMillis();
                    queue.put(String.valueOf(i++));
                    System.out.println("produce+: " + i + " spent(ms) " + (System.currentTimeMillis() - start));
                    Thread.sleep(rdm.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    long start = System.currentTimeMillis();
                    System.out.println("consume-: " + queue.take() + " spent(ms) " + (System.currentTimeMillis() - start));
                    Thread.sleep(rdm.nextInt(10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class MyArrayBlockingQueue<T> implements BlockingQueue<T> {
        T[] values;
        int capacity;
        int size;
        int idx = 0;
        ReadWriteLock lock = new ReentrantReadWriteLock();

        Lock writeLock = lock.writeLock();
//        Lock writeLock = new ReentrantLock();
        Condition emtpyCdt = writeLock.newCondition();
        Condition fullCdt = writeLock.newCondition();
        Lock readLock = lock.writeLock();

        public MyArrayBlockingQueue(int size) {
            this.capacity = size;
            values = (T[]) new Object[size];
        }

        @Override
        public void put(T t) throws InterruptedException {
            writeLock.lock();
            try {
                if (this.capacity == this.size) {
                    emtpyCdt.await();
                }
                values[idx++ % this.capacity] = t;
                this.size++;
                if(this.size == 1){
                    fullCdt.signal();
                }
            } finally {
                writeLock.unlock();
            }
        }

        @Override
        public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public T take() throws InterruptedException {
            writeLock.lock();
            try{
                if(this.size == 0){
                    fullCdt.await();
                }
                T v = values[(idx+capacity-size)%capacity];
                this.size--;
                if(this.size == this.capacity - 1){
                    emtpyCdt.signal();
                }
                return v;
            }finally{
                writeLock.unlock();
            }
        }

        @Override
        public boolean add(T t) {
            return false;
        }

        @Override
        public boolean offer(T t) {
            return false;
        }

        @Override
        public T remove() {
            return null;
        }

        @Override
        public T poll() {
            return null;
        }

        @Override
        public T element() {
            return null;
        }

        @Override
        public T peek() {
            return null;
        }

        @Override
        public T poll(long timeout, TimeUnit unit) throws InterruptedException {
            return null;
        }

        @Override
        public int remainingCapacity() {
            return 0;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            return null;
        }

        @Override
        public int drainTo(Collection<? super T> c) {
            return 0;
        }

        @Override
        public int drainTo(Collection<? super T> c, int maxElements) {
            return 0;
        }

		@Override
		public boolean removeIf(Predicate<? super T> filter) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Spliterator<T> spliterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Stream<T> stream() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Stream<T> parallelStream() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void forEach(java.util.function.Consumer<? super T> action) {
			// TODO Auto-generated method stub
			
		}
    }
}
