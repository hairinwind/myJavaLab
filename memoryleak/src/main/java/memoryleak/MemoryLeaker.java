package memoryleak;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemoryLeaker implements Runnable {
	
	private static CopyOnWriteArrayList<String> target = new CopyOnWriteArrayList<String>();
	
	public void run() {
		for (int i = 0 ; i<1000 ; i++) {
			Random random = new Random();
			target.add(String.valueOf(random.nextLong()));
		}
		
		System.out.println("MemoryLeaker size: " + target.size());
	}

}
