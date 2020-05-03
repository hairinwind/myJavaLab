package my.probability.questions;

import static my.probability.questions.RandomUtil.getRandomNumbersInRange;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.util.StopWatch;

/**
 * 6 questions
 * each question has 5 answers
 * guess randomly on all 6 questions
 * what is the probability to get exactly 4 questions correct
 */
public class Guess4In6 {
	
	private static int QUESTION_NUMBERS = 6;
	
	private static int EACH_QUESTION_ANSWER_NUMBERS = 5;
	
	private static int TOTAL_EXECUTE_TIME = 10000000; 
	
	public static void main(String[] args) {
		StopWatch sw = new StopWatch("stopwatch time");
		sw.start();
		
		List<Integer> correctResults = getRandomNumbersInRange(1, EACH_QUESTION_ANSWER_NUMBERS, QUESTION_NUMBERS);
		
		List<List<Integer>> allGuessAnswers = IntStream.range(0, TOTAL_EXECUTE_TIME).boxed()
				.map(i -> getRandomNumbersInRange(1, EACH_QUESTION_ANSWER_NUMBERS, QUESTION_NUMBERS))
				.collect(Collectors.toList());
		
		//each item is how many guessing is correct in one iteration for 6 questions.
		List<Integer> guessMatchCountList = allGuessAnswers.stream().map(guessAnswers -> countMatchAnswers(correctResults, guessAnswers))
			.collect(Collectors.toList());
		
		//exact match
		List<Result> results = IntStream.range(0, QUESTION_NUMBERS+1).boxed()
				.map(getCalculateFunction(Guess4In6::countExactlyMatch, guessMatchCountList))
				.map(Guess4In6::calculateProbability).collect(Collectors.toList());
		results.forEach(Guess4In6::printResult);
		
		//at least match
		List<Result> atleastResults = IntStream.range(3, 5).boxed()
				.map(getCalculateFunction(Guess4In6::countAtLeastMatch, guessMatchCountList))
				.map(Guess4In6::calculateProbability).collect(Collectors.toList());
		atleastResults.forEach(Guess4In6::printAtLeastResult);

		sw.stop();
		System.out.println("time: " + sw.getTotalTimeMillis() + "ms");
	}
	
	private static Function<Integer, Result> getCalculateFunction(BiFunction<List<Integer>, Integer, Long> matchFunction, 
			List<Integer> guessMatchCount) {
		return (expectCorrectAnswerNumber) -> {
			return new Result(expectCorrectAnswerNumber, matchFunction.apply(guessMatchCount, expectCorrectAnswerNumber));
		};
	}

	private static Result calculateProbability(Result result) {
		double probability = result.getMatchCount() * 1.0 / TOTAL_EXECUTE_TIME;
		result.setProbability(probability);
		return result;
	}
	
	private static void printResult(Result result) {
		System.out.println("probability for exactly " + result.getExpectCorrectAnswerNumber() + " correct answers: " 
				+ result.getProbability());
	}
	
	private static void printAtLeastResult(Result result) {
		System.out.println("probability for at least " + result.getExpectCorrectAnswerNumber() + " correct answers: " 
				+ result.getProbability());
	}
	
	/**
	 * exact match
	 * @param guessMatchCounts a list of correct guessing number in 6 questions
	 * @param matchNumber the expected correct guessing number
	 * @return a long value which is the count of the items 
	 * which guessing correct number matches the expected correct guessing number
	 */
	public static long countExactlyMatch(List<Integer> guessMatchCounts, int matchNumber) {
		return guessMatchCounts.stream().filter(matchCount -> matchCount == matchNumber).count();
	}
	
	/**
	 * at least match 
	 * @param guessMatchCounts guessMatchCounts a list of correct guessing number in 6 questions
	 * @param matchNumber matchNumber the minimum expected correct guessing number
	 * @return a long value which is the count of the items 
	 * which guessing correct number equals or great than the expected correct guessing number
	 */
	public static long countAtLeastMatch(List<Integer> guessMatchCounts, int matchNumber) {
		return guessMatchCounts.stream().filter(matchCount -> matchCount >= matchNumber).count();
	}

	private static int countMatchAnswers(List<Integer> expectedAnswers, List<Integer> guessAnswers) {
		int matchCount = 0;
		for (int i=0 ; i<expectedAnswers.size(); i++) {
			if (expectedAnswers.get(i).equals(guessAnswers.get(i))) {
				matchCount++;
			}
		}
		return matchCount;
	}
}

class Result {
	private Long matchCount;
	private Integer expectCorrectAnswerNumber;
	private double probability;
	public Result(Integer expectCorrectAnswerNumber, Long matchCount) {
		super();
		this.matchCount = matchCount;
		this.expectCorrectAnswerNumber = expectCorrectAnswerNumber;
	}
	public Long getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(Long matchCount) {
		this.matchCount = matchCount;
	}
	public Integer getExpectCorrectAnswerNumber() {
		return expectCorrectAnswerNumber;
	}
	public void setExpectCorrectAnswerNumber(Integer expectCorrectAnswerNumber) {
		this.expectCorrectAnswerNumber = expectCorrectAnswerNumber;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
