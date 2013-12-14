package memoryleak;

import java.util.Random;

public class AppLauncher {
	
	public static void main(String[] args) {
		Random r = new Random();
		for (int i=0 ; i< 300; i++) {
			int randomRun = r.nextInt(2);
			if (randomRun == 0) {
				new MemoryLeaker().run();
			} else {
				new NoMemoryLeaker().run();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("done...");
	}
}
