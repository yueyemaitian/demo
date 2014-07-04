//package com.tmall.buy.jmh;
//
//import org.apache.commons.math3.analysis.function.Cos;
//import org.apache.commons.math3.analysis.function.Sin;
//import org.apache.commons.math3.analysis.function.Tan;
//import org.openjdk.jmh.annotations.*;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.RecursiveTask;
//import java.util.concurrent.TimeUnit;
//
//@BenchmarkMode(Mode.Throughput)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 20, time = 3, timeUnit = TimeUnit.SECONDS)
//@Fork(1)
//@State(Scope.Benchmark)
//public class FJPBenchmark {
//
//    @Param({"200", "400", "800", "1600"})
//    public int N;
//
//    public List<RecursiveTask<Double>> tasks;
//    public ForkJoinPool pool = new ForkJoinPool();
//
//    @Setup
//    public void init() {
//        Random r = new Random();
//        r.setSeed(0x32106234567L);
//        tasks = new ArrayList<RecursiveTask<Double>>(N * 3);
//
//        for (int i = 0; i < N; i++) {
//            tasks.add(new Sin(r.nextDouble()));
//            tasks.add(new Cos(r.nextDouble()));
//            tasks.add(new Tan(r.nextDouble()));
//        }
//    }
//
//    @GenerateMicroBenchmark
//    public double forkJoinTasks() {
//        for (RecursiveTask<Double> task : tasks) {
//            pool.submit(task);
//        }
//        double sum = 0;
//        Collections.reverse(tasks);
//        for (RecursiveTask<Double> task : tasks) {
//            sum += task.join();
//        }
//        return sum;
//    }
//
//    @GenerateMicroBenchmark
//    public double computeDirectly() {
//        double sum = 0;
//        for (RecursiveTask<Double> task : tasks) {
//            sum += ((DummyComputableThing<Double>) task).dummyCompute();
//        }
//        return sum;
//    }
//
//    static abstract class DummyComputableThing<T> extends RecursiveTask<T>{
//       public T dummyCompute(){
//           T r = this.compute();
//           return r;
//       }
//    }
//    static class Sin extends DummyComputableThing{
//
//        private final double value;
//
//        public Sin(double value){
//            this.value = value;
//        }
//        @Override
//        protected Object compute() {
//            return Math.sin(this.value);
//        }
//    }
//
//    static class Cos extends DummyComputableThing{
//
//        private final double value;
//
//        public Cos(double value){
//            this.value = value;
//        }
//        @Override
//        protected Object compute() {
//            return Math.cos(this.value);
//        }
//    }
//
//    static class Tan extends DummyComputableThing{
//
//        private final double value;
//
//        public Tan(double value){
//            this.value = value;
//        }
//        @Override
//        protected Object compute() {
//            return Math.tan(this.value);
//        }
//    }
//}