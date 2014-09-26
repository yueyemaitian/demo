package com.tmall.buy.monitor;

import sun.jvm.hotspot.gc_interface.CollectedHeap;
import sun.jvm.hotspot.memory.Universe;
import sun.jvm.hotspot.runtime.JavaThread;
import sun.jvm.hotspot.runtime.JavaThreadState;
import sun.jvm.hotspot.runtime.VM;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tianmai.fh
 * @date 2014-03-20 14:20
 */
public class VmMonitor2  {
    public static void main(String[] args) {
        VmMonitor2 monitor = new VmMonitor2();
        monitor.run();

    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VM vm = VM.getVM();
            StringBuilder sb = new StringBuilder(500);
            sb.append("CPU: ").append(vm.getCPU()).append("\n");

            Universe universe = vm.getUniverse();
            CollectedHeap heap = universe.heap();

//            sb.append("ObjectHeap: ").append(vm.getObjectHeap());
            sb.append("Heap Capacity: ").append(heap.capacity()).append("\n");
            sb.append("Heap Used Capacity: ").append(heap.used()).append("\n");
            JavaThread thread = vm.getThreads().first();
            int threadCnt = 0;
            Map<JavaThreadState,Integer> stateMap = new HashMap<JavaThreadState,Integer>();
            while(thread != null){
                threadCnt++;
                thread.getThreadState();
                Integer cnt = stateMap.get(thread.getThreadState());
                if(cnt == null){
                    cnt = 0;
                }
                stateMap.put(thread.getThreadState(), ++cnt);
                thread = thread.next();
            }
            sb.append("Thread Count: ").append(threadCnt).append("\n");
            sb.append("\t");
            for(Map.Entry<JavaThreadState,Integer> entry : stateMap.entrySet()){
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\t");
            }
            sb.append("\n");
            System.out.println(sb);

//            ((G1CollectedHeap)heap).
//            ((GenCollectedHeap)heap).
//            ((ParallelScavengeHeap)heap).youngGen()
//            G1CollectedHeap
//            GenCollectedHeap
//            ParallelScavengeHeap
//            SharedHeap
        }
    }
}
