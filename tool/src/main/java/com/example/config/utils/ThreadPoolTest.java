package com.example.config.utils;

/**   
* @Description:
* @author suicaijiao  
* @date 2019年5月6日  
*/
public class ThreadPoolTest {
	
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName());
		myThread t1 = new myThread();
		t1.start();
		
		MyRunnable r = new MyRunnable();
		Thread thread = new Thread(r);
		thread.start();
	}
	
	public static class myThread extends Thread{
		@Override
		public void run() {
			for(int i=0;i<10; i++){
				System.out.println(Thread.currentThread().getName()+"**"+System.currentTimeMillis()+"**"+i);
			}
		}
	}
	
	static class MyRunnable implements Runnable{
		@Override
		public void run() {
			for(int i=0;i<10; i++){
				System.out.println(Thread.currentThread().getName()+"**"+System.currentTimeMillis()+"**"+i);
			}
			
		}
	}
}
