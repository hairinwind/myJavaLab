package my.javalab.thread;

public class WaitNotify {

	public static void main(String[] args) {
		ThreadX b = new ThreadX();
		b.start();
		
		synchronized(b) {
			try {
				System.out.println("Waiting fro b to complete...");
				b.wait();
				System.out.println("after wait...");
			}catch (InterruptedException e) {
				
			}
			System.out.println("Total: " + b.total);
		}
	}

}

class ThreadX extends Thread {
	int total;
	public void run() {
		synchronized(this) {
			System.out.println(this);
			for (int i=0;i<100;i++) {
				total += i;
//				try {
//					this.wait();
//					notifyAll();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			System.out.println("synchronize block is done");
		}
	}
}
