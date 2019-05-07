package stream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class ParallelStreamNotFaster {

	public static void main(String[] args) throws IOException {
		System.out.println("\n2.1 -------------------");
		String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/alice.txt")), StandardCharsets.UTF_8); // Read
																											// file
																											// into
																											// string
		List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));
		System.out.println("total words " + words.size());

		// implementation 1
		monitorRunFunction(words, wordList -> {
			return wordList.stream().filter(w -> w.length() > 12).count();
		});

		// implementation 2
		monitorRunFunction(words, wordList -> {
			AtomicInteger ai = new AtomicInteger(0);
			wordList.parallelStream().forEach(s -> {
				if (s.length() > 12)
					ai.addAndGet(1);
			});
			return ai.longValue();
		});

		// implementation 3
		monitorRunFunction(words, wordList -> {
			return wordList.parallelStream().filter(w -> w.length() > 12).count();
		});
	}

	public static void monitorRunFunction(List<String> words, Function<List<String>, Long> f) {
		long start = System.currentTimeMillis();
		Long result = f.apply(words);
		long end = System.currentTimeMillis();
		System.out.println("count: " + result);
		System.out.println((end - start) + " ms");
	}

}
