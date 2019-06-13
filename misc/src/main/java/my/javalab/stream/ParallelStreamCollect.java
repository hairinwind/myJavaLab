package my.javalab.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParallelStreamCollect {
	List<Integer> matched = new ArrayList<>();
	List<Integer> elements = new ArrayList<>();
	public void sampleFunction() {
	    for (int i=0; i< 10000; i++) {
	        elements.add(i);
	    }

	    // Here is the bad code
	    elements.parallelStream().forEach(
	        e -> {
	            if (e>=100) {
	                matched.add(e); // this throws ConcurrentModificationException
	                // also, here the lambda tries to modify the external data "matched" 
	                // which shall be avoided
	            }
	        }
	    );
	    
	    // Here is the solution
	    matched = elements.parallelStream().filter(e -> e>=100).collect(Collectors.toList()); 
	    
	    System.out.println(matched.size()); 
	}

	public static void main(String[] args) {
		ParallelStreamCollect p = new ParallelStreamCollect();
		p.sampleFunction();
	}

}
