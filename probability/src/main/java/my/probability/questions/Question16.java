package my.probability.questions;

import static my.probability.questions.RandomUtil.getRandomNumberInRange;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Question16 {
	
//	private static int BLUE_BLOCK_NUMBER = 4;
//	private static int GREEN_BLOCK_NUMBER = 3;
//	private static int RED_BLOCK_NUMBER = 1;
	
	private static int totalRandom = 3;
	
	public static void main(String[] args) {
		int total = 1000000;
		int count = 0;
		for (int i=0; i<total; i++) {
			List<String> result = randomTake3Blocks();
			if (isQualified(result)) {
				count ++;
			}
		}
		double probability = count * 1.0D / total;
		System.out.println("probability: " + probability);
	}
	
	private static boolean isQualified(List<String> result) {
		if (result.size() != 3) {
			throw new RuntimeException("did not pick up 3 blocks...");
		}
		return result.contains("BLUE") && result.contains("GREEN") && result.contains("RED");
	}

	private static List<String> randomTake3Blocks() {
		Set<Integer> randomSet = new HashSet<Integer>();
		while (randomSet.size() < totalRandom) {
			int randomNumber = getRandomNumberInRange(1,8);
			randomSet.add(randomNumber);
		}
		
		List<String> randomList = randomSet.stream().map(Question16::getCategory).collect(Collectors.toList());
		
		System.out.println(randomSet + " : " + randomList);
		return randomList;
	}

	private static String getCategory(int randomNumber) {
		if (randomNumber <= 4) {
			return "BLUE";
		} else if (randomNumber <= 7) {
			return "GREEN";
		} else {
			return "RED";
		}
	}
	
	
}
