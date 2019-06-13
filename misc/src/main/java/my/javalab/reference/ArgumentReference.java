package my.javalab.reference;

public class ArgumentReference {
	public static void main(String[] args) {
		ClassA a = new ClassA();
		a.x = 10;
		int y = 20;
		foo(a, y);
		System.out.println(a.x);
		System.out.println(y);
	}
	
	public static void foo(ClassA a, int y) {
		a.x = 100;
		y = 30;
		a = null;
	}
	
}

class ClassA {
	public int x;
}