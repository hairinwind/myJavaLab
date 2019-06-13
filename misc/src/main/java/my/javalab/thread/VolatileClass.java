package my.javalab.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VolatileClass {
	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(5,5,1, 
			TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), 
			new ThreadPoolExecutor.CallerRunsPolicy());
		
	static {
		pool.allowCoreThreadTimeOut(true);
	}

	private static boolean b = false; //this is the flag
	// without volatile, the threadpool cannot see the changes, so it keeps running
	// if inside the f() function, System.out.println() is executed, because it is a synchronized function, 
	// it would update the thread variable from main memory, so it can see the b is true and the thread pool is going to the end
	
//	private static volatile boolean b = false;
	//with volatile, the threaadpool can see the change right away and the thread pool can end right after the value is changed.
	
	public static void main(String[] args) throws InterruptedException {
		pool.execute(new Runnable(){
			@Override
			public void run() {
				int i = 0;
				while(!b) {
					i++;
					f(i);
				}
				System.out.println("thread over");
			}
		});
		Thread.sleep(500);
		System.out.println("main over");
		b = Boolean.TRUE;
		System.out.println("value b is " + b);
	}
	
	static void f(int i) {
//		System.out.println(i);
		// System.out.println is a synchronized function, it would update the thread variables from the main memory
	}

}
