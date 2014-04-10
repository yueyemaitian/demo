package com.tmall.buy;

public class EqualsTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        for (int k = 0; k < 5; k++) {
            int[] a = new int[10000000];
            int[] b = new int[10000000];
            for (int i = 0; i < a.length; i++) {
                setValue(a, i);
                setValue(b, i);
                new Object();
            }
            System.out.println(a.equals(b));
        }

        System.out.println(false & "aaa".equals(null));

        System.out.println((String) null);

    }

    public static void setValue(int[] arr, int idx) {
        arr[idx] = idx;
    }

}
