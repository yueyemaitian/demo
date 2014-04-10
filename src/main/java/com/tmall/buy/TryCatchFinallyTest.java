package com.tmall.buy;

import java.io.FileInputStream;
import java.io.IOException;

public class TryCatchFinallyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		FileInputStream fips = null;
		try {
			fips = new FileInputStream("hello.txt");
			fips.read();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fips != null) {
				try {
					fips.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					fips = null;
				}
			}
		}
	}

	public static int justTestWithReturn() {
		FileInputStream fips = null;
		try {
			fips = new FileInputStream("hello.txt");
			return fips.available();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (fips != null) {
				try {
					fips.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void justTestWithoutReturn() throws IOException,Exception {
		FileInputStream fips = null;
		try {
			fips = new FileInputStream("hello.txt");
			fips.available();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fips.close();
		}
	}

}
