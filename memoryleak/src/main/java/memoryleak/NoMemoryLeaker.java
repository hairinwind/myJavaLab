package memoryleak;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class NoMemoryLeaker implements Runnable {
	
	private static CopyOnWriteArrayList<String> target = new CopyOnWriteArrayList<String>();
	
	public void run() {
		target.clear();
		for (int i = 0 ; i<100 ; i++) {
			Random random = new Random();
			target.add(String.valueOf(random.nextLong()));
		}
		
		System.out.println("NoMemoryLeaker size: " + target.size());
	}

}
