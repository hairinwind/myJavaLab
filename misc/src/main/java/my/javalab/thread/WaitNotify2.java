package my.javalab.thread;

/**
 * This example explains how two threads work in turn
 * by wait and notifyAll
 *
 */
public class WaitNotify2 {

	public static void main(String[] args) throws InterruptedException {
		Total total = new Total();
		ThreadA a = new ThreadA(total);
		ThreadB b = new ThreadB(total);
		a.start();
		b.start();
		a.join();
		b.join();
		System.out.println("done, total is " + total.value);
	}

}

class Total{
	int value;
}

class ThreadA extends Thread {
	private Total total;
	public ThreadA(Total total) {
		this.total = total;
	}
	public void run() {
		synchronized(total) {
			while (total.value < 100) {
				if (total.value %2 == 0) {
					total.value++;
					System.out.println("ThreadA is running..." + total.value);
					total.notifyAll();
				} else {
					try {
						System.out.println("ThreadA is waiting..." + total.value);
						total.wait();
						System.out.println("ThreadA is woken up..." + total.value);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}

class ThreadB extends Thread {
	private Total total;
	public ThreadB(Total total) {
		this.total = total;
	}
	public void run() {
		synchronized(total) {
			while(total.value < 100) {
				if (total.value %2 == 1) {
					total.value++;
					System.out.println("ThreadB is running..." + total.value);
					total.notifyAll();
				} else {
					try {
						System.out.println("ThreadB is waiting..." + total.value);
						total.wait();
						System.out.println("ThreadB is woken up..." + total.value);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
