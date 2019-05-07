package my.javalab.plusplus;

public class PlusPlus {
	public static void main (String[] args) {
		int i = 0; 
		i = i++ + ++i;
		System.out.println(i);
		
		i=0;
		i = i-- - --i;
		System.out.println(i);
		
		i=0;
		i = --i - i--;
		System.out.println(i);
		
		i=0;
		i= ++i + i++;
		System.out.println(i);
				
	}
}
