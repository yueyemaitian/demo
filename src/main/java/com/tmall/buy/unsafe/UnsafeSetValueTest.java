package com.tmall.buy.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * -Xcomp
 * @author tianmai.fh
 * @date 2014-03-12 16:43
 */
public class UnsafeSetValueTest {

    static int COUNT = 50000000;
    static int LENGTH = 1024;
    static long BEAN_A_OFFSET;
    static Unsafe unsafe;
    static Field aField;
    static Field bField;

    static long BEAN_B_OFFSET;

    static {
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            Method objectFieldOffsetMethod = Unsafe.class.getDeclaredMethod("objectFieldOffset", new Class[]{Field.class});
            objectFieldOffsetMethod.setAccessible(true);

            BEAN_A_OFFSET = unsafe.objectFieldOffset(Bean.class.getDeclaredField("a"));
            BEAN_B_OFFSET = unsafe.objectFieldOffset(Bean.class.getDeclaredField("b"));
            aField = Bean.class.getDeclaredField("a");
            aField.setAccessible(true);

            bField = Bean.class.getDeclaredField("b");
            bField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static class Bean {
        private int a;
        private int b;
        private int c[] = new int[LENGTH];
    }


    public static void testFieldOffsetAssign() throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Bean bean = new Bean();
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            unsafe.putInt(bean,BEAN_A_OFFSET,i);
            unsafe.putInt(bean,BEAN_B_OFFSET,i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Offset spend: " + (end - start) + " ms");

    }


    public static void testReflectAssign() throws IllegalAccessException {
        Bean bean = new Bean();
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            aField.set(bean, i);
            bField.set(bean, i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Reflect spend: " + (end - start) + " ms");

    }

    public static void testDirectAssign() throws IllegalAccessException {
        Bean bean = new Bean();
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            bean.a = i;
            bean.b = i;
        }
        long end = System.currentTimeMillis();
        System.out.println("Direct spend: " + (end - start) + " ms");

    }

    public static void main(String[] args) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < 5; i++) {
            testDirectAssign();
            testReflectAssign();
            testFieldOffsetAssign();
            System.out.println();
        }
    }
}
