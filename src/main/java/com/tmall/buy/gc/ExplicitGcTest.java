package com.tmall.buy.gc;

import java.util.ArrayList;
import java.util.List;

public class ExplicitGcTest {
	private static int SIZE = 4 * 1024 * 1024;
	private static List<Long> list = new ArrayList<Long>(SIZE);

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < SIZE; i++) {
			list.add((long) i);
		}
		System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
		list.clear();;
		System.gc();
		Thread.sleep(10000);
		for (int i = 0; i < SIZE; i++) {
			list.add((long) i);
		}
		Thread.sleep(10000);
	}

}
