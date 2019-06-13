package my.javalab.basic;

/**
 * Although we usually don't access non-static property from static method 
 * but actually it is allowed, as long as the static method know which instance it need to access
 * It cannot access the non-static property directly
 */
public class StaticAccessNonStaticProperty {
	
	int x = 123; // this is a non-static property
	static StaticAccessNonStaticProperty s = new StaticAccessNonStaticProperty();

	public static void main(String[] args) {
		System.out.println(s.x); // access the non-static property as long as it knows which instance...
	}

}
