package com.tmall.buy.cacheline;

/**
 * -Xms2500m -Xmx2500m -Xcomp -Xmn2g -XX:NewRatio=10
 * @author tianmai.fh
 * @date 2014-03-12 16:55
 */
public class CacheLineTest {

    public static final int COUNT = 3;
    public static void main(String[] args) {
        int[] arrs = new int[64 * 1024 * 1024 * 4];  //1g的空间，通过参数设值全放到了eden取，避免gc对测试结果的影响
        equivalentWidth(arrs);
        fullLoop(arrs);
    }

    /**
     * 全循环，看跨cache line和不跨cache line的时候，花费时间对比
     * @param arrs
     */
    public static void fullLoop(int[] arrs){
        int forLen = 256;
        int forAssembly = 0;
        while (forAssembly++ < COUNT) { // 循环三次，避免第一次代码优化前的影响
            for (int i = 1; i <= forLen; i *= 2) {
                long start = System.currentTimeMillis();
                for (int j = 0, size = arrs.length; j < size; j += i) {
                    arrs[j] = j * 3;
                }
                long end = System.currentTimeMillis();
                System.out.println("Full, factor: " + i + " spent " + (end - start) + " ms");
            }
            System.out.println();
        }
    }
    /**
     * 每次循环计算次数相同，看跨cache line和不跨cache line的时候，花费时间对比
     * @param arrs
     */
    public static void equivalentWidth(int[] arrs){
        int forLen = 256;
        int breakWidth = arrs.length / 256;
        int forAssembly = 0;
        while (forAssembly++ < COUNT) { // 循环三次，避免第一次代码优化前的影响
            for (int i = 1; i <= forLen; i *= 2) {
                long start = System.currentTimeMillis();
                int cnt = 0;
                for (int j = 0, size = arrs.length; j < size; j += i) {
                    arrs[j] = j;
                    if (++cnt > breakWidth) {     //每次循环就access这么多数据
                        break;
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("Equivalent Witdh, factor: " + i + " spent " + (end - start) + " ms");
            }
            System.out.println();
        }
    }
}
