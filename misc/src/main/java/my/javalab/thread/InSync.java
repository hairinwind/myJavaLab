package my.javalab.thread;

public class InSync extends Thread {
	StringBuffer letter;

	public InSync(StringBuffer letter) {
		this.letter = letter;
	}

	public void run() {
		 synchronized(letter) {
		   for(int i = 1;i<=10 ;++i) {
			   System.out.println(letter);       
			   System.out.println("Thread id: " + Thread.currentThread().getId());       
			   char temp = letter.charAt(0);       
			   ++temp;         
			   // Increment the letter in StringBuffer:       
			   letter.setCharAt(0, temp);     
		   } 
		 }
	}

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer("A");
		new InSync(sb).start();
		//The second thread won't get the lock before the first thread is done, same for the next thread
		new InSync(sb).start();
		new InSync(sb).start();
	}
}
