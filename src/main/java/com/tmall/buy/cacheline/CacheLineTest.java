package com.tmall.buy.cacheline;

/**
 * -Xms2500m -Xmx2500m -Xcomp -Xmn2g -XX:NewRatio=10
 * @author tianmai.fh
 * @date 2014-03-12 16:55
 */
public class CacheLineTest {

    public static final int COUNT = 3;
    public static void main(String[] args) {
        int[] arrs = new int[64 * 1024 * 1024 * 4];  //1g�Ŀռ䣬ͨ��������ֵȫ�ŵ���edenȡ������gc�Բ��Խ����Ӱ��
        equivalentWidth(arrs);
        fullLoop(arrs);
    }

    /**
     * ȫѭ��������cache line�Ͳ���cache line��ʱ�򣬻���ʱ��Ա�
     * @param arrs
     */
    public static void fullLoop(int[] arrs){
        int forLen = 256;
        int forAssembly = 0;
        while (forAssembly++ < COUNT) { // ѭ�����Σ������һ�δ����Ż�ǰ��Ӱ��
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
     * ÿ��ѭ�����������ͬ������cache line�Ͳ���cache line��ʱ�򣬻���ʱ��Ա�
     * @param arrs
     */
    public static void equivalentWidth(int[] arrs){
        int forLen = 256;
        int breakWidth = arrs.length / 256;
        int forAssembly = 0;
        while (forAssembly++ < COUNT) { // ѭ�����Σ������һ�δ����Ż�ǰ��Ӱ��
            for (int i = 1; i <= forLen; i *= 2) {
                long start = System.currentTimeMillis();
                int cnt = 0;
                for (int j = 0, size = arrs.length; j < size; j += i) {
                    arrs[j] = j;
                    if (++cnt > breakWidth) {     //ÿ��ѭ����access��ô������
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
