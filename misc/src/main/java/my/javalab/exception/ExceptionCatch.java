package my.javalab.exception;

public class ExceptionCatch {

	public static void main(String[] args) throws Exception {
		try {
			throw new ChildException();
		}catch (ChildException ce) {
			System.out.println(ce.toString());
		}catch (ParentException pe) { //catch parent before child exception is not allowed, compiling error
			System.out.println(pe.toString());
		}finally {
			System.out.println("finally is running");
			throw new ParentException();
		}

	}

}
