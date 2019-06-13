package my.javalab.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ClockwiseToString {
	
	private List<String> getClockwiseString(int[][] matrix) {
		List<String> result = new ArrayList<String>();
		String firstRowString = intArrayToString(matrix[0]);
		result.add(firstRowString);
		
		//get the sub-Matrix, which excludes the first row
		int[][] subMatrix = getSubMatrix(matrix, 1);
		if (subMatrix != null) {			
//			printArray("subMatrix", subMatrix);
			//rotate it countClockwise
			int[][] rotatedSubMatrix = rotateMatrixCounterclockwise(subMatrix);
			
			List<String> theRest = getClockwiseString(rotatedSubMatrix);
			if (theRest != null) {
				result.addAll(theRest);
			}
		}
		
		return result;
	}
	
	private int[][] getSubMatrix(int[][] matrix, int startRowIndex) {
		int matrixRows = matrix.length;
		if (startRowIndex >= matrixRows) {
			return null;
		}
		int[][] subMatrix = new int[matrixRows - 1][];
		for (int i= startRowIndex; i < matrixRows; i++) {
			subMatrix[i-startRowIndex] = matrix[i];
		}
		return subMatrix;
	}

	private String intArrayToString(int[] intArray) {
		return Arrays.stream(intArray)
		        .mapToObj(String::valueOf)
		        .collect(Collectors.joining(", "));
	}
	
	private int[][] rotateMatrixCounterclockwise(int[][] matrix) {
		int y = matrix.length;
		int x = matrix[0].length;
		int[][] rotatedMatrix = new int[x][y];
		for (int x1 = x-1; x1>-1; x1--) {
			int[] rotatedMatrixOneRow = rotatedMatrix[x-1-x1];
			for(int y1 = 0; y1 < y; y1++) {
				rotatedMatrixOneRow[y1] = matrix[y1][x1];
			}
		}
//		printArray("rotatedMatrix", rotatedMatrix);
		return rotatedMatrix;
	}

	private static void printArray(String title, int[][] matrix) {
		
		System.out.println("=== " + title + " ===");
		for (int[] row: matrix) {
			System.out.println(java.util.Arrays.toString(row));
		}
		System.out.println("==============");
	}
	
	private static int[][] constructMatrix(int y, int x) {
		int[][] matrix = new int[y][x];
		Random random = new Random();
		for (int y1=0; y1<y; y1++) {
			for (int x1=0; x1<x; x1++) {
				matrix[y1][x1] = random.nextInt(100);
			}
		}
		return matrix;
	}
	
	public static void main(String[] args) {
		int x = 4;
		int y = 3; // you can change the dimension as you want
		int[][] matrix = constructMatrix(y, x);
		printArray("Matrix", matrix);
		
		ClockwiseToString clockwiseToString = new ClockwiseToString();
		List<String> clockwiseString = clockwiseToString.getClockwiseString(matrix);
		System.out.println(clockwiseString);
	}

}
