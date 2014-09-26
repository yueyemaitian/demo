package com.tmall.buy.structure;

import sun.jvm.hotspot.gc_interface.CollectedHeap;
import sun.jvm.hotspot.memory.ConcurrentMarkSweepGeneration;
import sun.jvm.hotspot.memory.GenCollectedHeap;
import sun.jvm.hotspot.memory.Generation;
import sun.jvm.hotspot.oops.*;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;
import sun.jvm.hotspot.utilities.SystemDictionaryHelper;

/**
 * 打印对象的内存布局
 */
public class PrintObjectTest extends Tool {
    public static void main(String[] args) throws InterruptedException {
        PrintObjectTest test = new PrintObjectTest();
//        test.start(args);
        test.stop();
    }

    @Override
    public void run() {
        this.printSpecificKlazz("com.tmall.buy.structure.FieldOffsetTest$MyClass");
        this.printCMSHeapInfo();
    }

    private void printSpecificKlazz(String fullClazzName){
        VM vm = VM.getVM();
        ObjectHeap objHeap = vm.getObjectHeap();
        HeapVisitor heapVisitor = new HeapPrinter(System.out);
        //观察特定对象
        Klass klass = SystemDictionaryHelper.findInstanceKlass(fullClazzName);
        if(klass != null){
            objHeap.iterateObjectsOfKlass(heapVisitor, klass, false);
        }
    }

    private void printCMSHeapInfo(){
        CollectedHeap heap = VM.getVM().getUniverse().heap();
        if(heap instanceof GenCollectedHeap){
            GenCollectedHeap genHeap = (GenCollectedHeap) heap;
            System.out.println("======nGens:" + genHeap.nGens());
            Generation newGen = genHeap.getGen(0);
            Generation oldGen = genHeap.getGen(1);
            if(newGen instanceof ConcurrentMarkSweepGeneration){
                ConcurrentMarkSweepGeneration newCmsGen = (ConcurrentMarkSweepGeneration)newGen;
                System.out.println("newCmsGen.cmsSpace().free():" + newCmsGen.cmsSpace().free());
                System.out.println("newCmsGen.free():" + newCmsGen.free());
            }
            if(oldGen instanceof ConcurrentMarkSweepGeneration){
                ConcurrentMarkSweepGeneration oldCmsGen = (ConcurrentMarkSweepGeneration)oldGen;
                System.out.println("oldCmsGen.cmsSpace().free():" + oldCmsGen.cmsSpace().free());
                System.out.println("oldCmsGen.free():" + oldCmsGen.free());

            }
        }
    }

    /**
     * 观察数组对象
     */
    private void printObjArray(){
        VM vm = VM.getVM();
        ObjectHeap objHeap = vm.getObjectHeap();
        HeapVisitor heapVisitor = new HeapPrinter(System.out);
        objHeap.iterate(heapVisitor,new ObjectHeap.ObjectFilter() {
            @Override
            public boolean canInclude(Oop oop) {
                return oop.isObjArray();
            }
        });
        objHeap.iterate(heapVisitor);
    }
}
//        klass = SystemDictionaryHelper.findInstanceKlass(Integer.class.getName());
//        objHeap.iterateObjectsOfKlass(heapVisitor, klass, false);

//        System.out.println(klass.getClass().getName());
//        InstanceKlass[] instanceKlasses = SystemDictionaryHelper.findInstanceKlasses("java.lang.Long");
//        for(InstanceKlass instClass : instanceKlasses){
//            objHeap.iterateObjectsOfKlass(heapVisitor, instClass.getKlass(), false);
//        }

//try {
//        Thread.sleep(2000);
//        } catch (InterruptedException e) {
//        e.printStackTrace();
//        }
//Universe universe = vm.getUniverse();
//CollectedHeap heap = universe.heap();
//    puts("GC heap name: " + heap.kind());
//        if (heap instanceof ParallelScavengeHeap) {
//        ParallelScavengeHeap psHeap = (ParallelScavengeHeap) heap;
////            PSOldGen psOldGen = psHeap.oldGen();
////            MutableSpace oldGenSpace = psOldGen.objectSpace();
//
//
//        PSPermGen perm = psHeap.permGen();
//        MutableSpace permObjSpace = perm.objectSpace();
//        puts("Perm gen: [" + permObjSpace.bottom() + ", " + permObjSpace.end() + ")");
//        long permSize = 0;
//        for (VM.Flag f : VM.getVM().getCommandLineFlags()) {
//        if ("PermSize".equals(f.getName())) {
//        permSize = Long.parseLong(f.getValue());
//        break;
//        }
//        }
//        puts("PermSize7: " + permSize);
//        }
//        puts();
//private static void puts() {
//    System.out.println();
//}
//
//    private static void puts(String s) {
//        System.out.println(s);
//    }