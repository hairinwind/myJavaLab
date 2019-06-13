package my.javalab.decimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OctalHex {

	public static void main(String[] args) {
		System.out.println(itoa(150, 8)); //226
		System.out.println(itoa(150, 10)); //150
		System.out.println(itoa(150, 16)); //96
		System.out.println(itoa(11, 16)); //B
		System.out.println(itoa(16, 16)); //10
		System.out.println(itoa(17, 16)); //11
		System.out.println(itoa(176, 16)); //B0
	}
	
	/**
	 * Write the code for the following function, without using any built-in functions.
	 * Where value is the integer to convert, and base is octal, decimal, or hex.
	 * @param value integer
	 * @param base 8-octal, 10-decimal, 16-hex
	 * @return 
	 */
	public static String itoa(int value, int base) {
		List<Integer> result = convert(value, base);
		Collections.reverse(result);
		return result.stream()
        	.map(v -> remainderToString(v, base))
        	.collect(Collectors.joining());
	}
	
	/**
	 * Here is one example
	 * 150 / 8 = 18 ... 6
	 * 18 / 8 = 2    ...2
	 * 2 / 8 = 0     ...2
	 * the octal is 226, from the bottom to the top 
	 */
	public static List<Integer> convert(int value, int base) {
		List<Integer> result = new ArrayList<Integer>();
		
		int remainder = value % base;
		result.add(remainder);
		
		int quotient = value / base;
		if (quotient > 0) {
			result.addAll(convert(quotient, base));
		} 
		
		return result;
	}
	
	public static String remainderToString(int remainder, int base) {
		if(remainder > 9) {
			char c = (char)(65 + remainder -10);
			return Character.toString(c);
		} else {
			return String.valueOf(remainder);
		}
		
	}
	
}
