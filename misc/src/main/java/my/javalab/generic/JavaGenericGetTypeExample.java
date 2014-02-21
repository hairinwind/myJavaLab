package my.javalab.generic;

import java.lang.reflect.ParameterizedType;

public class JavaGenericGetTypeExample<E> {
	
	private String genericType;
	
	

	public String getGenericType() {
		return genericType;
	}

	public void setGenericType(String genericType) {
		this.genericType = genericType;
	}

	public JavaGenericGetTypeExample() {
//		Class<E> typeClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
//	    Type type = genericSuperclass.getActualTypeArguments()[0];
//		this.genericType = typeClass.getName();
	}
	
	public static void main(String[] args) {
		JavaGenericGetTypeExample<String> stringExample = new JavaGenericGetTypeExample<String>();
		System.out.println("generic type: " + stringExample.getGenericType());
	}

}
