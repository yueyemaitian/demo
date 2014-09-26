package com.tmall.buy.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * cms gc测试，VM参数：
 * -Xmx1032m -Xms1032m -Xmn516m -XX:NewRatio=10
 * -verbose:gc -Xloggc:./gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 * -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=80 -XX:-UseCMSInitiatingOccupancyOnly
 * 计算出来各个区占用空间情况是：
 *      Eden:430m; S0:43m; S1:43m; Tenured:516m
 * @author tianmai.fh
 * @date 2014-03-17 19:19
 */
@SuppressWarnings("unused")
public class CmsGcTest {

    static List<Integer[]> longList = new ArrayList<Integer[]>();
    static final int M_280_INT_SIZE = 70 * 1024 * 1024;   //280M = 4byte * 70M
    static final int M_112_INT_SIZE = 24 * 1024 * 1024;   //112M = 4byte * 28M

    static void gcTest() throws InterruptedException {
//        Thread.sleep(10000);
		Object[] m280 = new Object[M_280_INT_SIZE];
        m280 = null;
        for (int i = 0; i < 3; i++) {
            Integer[] m112 = new Integer[M_112_INT_SIZE];
            longList.add(m112);
            Object[] m2801 = new Object[M_280_INT_SIZE];
            m2801 = null;
//            Thread.sleep(5000);
        }
//        System.gc();
//        Thread.sleep(20000);
//        for (int i = 0; i < 3; i++) {
//            longList.add(new Integer[1024 * 1024]);
//            Integer[] m140 = new Integer[M_280_INT_SIZE];
//            m140 = null;
//        }
        Thread.sleep(20000);
    }

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException {
        gcTest();
    }
}
