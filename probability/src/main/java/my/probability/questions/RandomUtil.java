package my.probability.questions;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomUtil {

	public static Integer getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	public static List<Integer> getRandomNumbersInRange(int min, int max, int number) {
		return IntStream.range(0, number)
				.map(i -> getRandomNumberInRange(min, max))
				.boxed()
				.collect(Collectors.toList());
	}
	
	public static void main(String[] args) {
		System.out.println(getRandomNumbersInRange(1,5,6));
	}
}
