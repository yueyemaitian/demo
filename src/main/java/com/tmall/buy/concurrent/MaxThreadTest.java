package com.tmall.buy.concurrent;

public class MaxThreadTest {

	public static void main(String[] args) {

		for(int i = 0;i < 100000;i++){
			if(i%2000 == 0 && i > 0){
				System.out.println(i);
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			new Thread(String.valueOf(i)){

				@Override
				public void run() {
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}.start();
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
