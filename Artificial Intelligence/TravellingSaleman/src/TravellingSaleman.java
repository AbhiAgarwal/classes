import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class TravellingSaleman {
	// Geometric (Euclidean) distance
	// The distance from [Ux,Uy] to [Vx,Vy] is sqrt((Ux-Vx)^2 + (Uy-Vy)^2)
	public static double currentDistance(XYC cityOne, XYC cityTwo){
		// Minus the values
		double XsCityOne = Math.abs(cityOne.getX() - cityTwo.getX());
		double XsCityTwo = Math.abs(cityOne.getY() - cityTwo.getY());
		// Square Them
		double XsSquared = Math.pow(XsCityOne, 2);
		double YsSquared = Math.pow(XsCityTwo, 2);
		// Add them and Square Root them
		double returnValue = Math.sqrt(XsSquared + YsSquared);
		return returnValue;
	}
	
	public static void swap(ArrayList<XYC> coArray, int one, int two){
		Collections.swap(coArray, one, two);
		/*
		XYC elementOne = coArray.get(one);
		coArray.set(one, coArray.get(two));
		coArray.set(two, elementOne);
		return coArray;
		 */
	}
	
	public static double calculateTotalDistance(ArrayList<XYC> coArray){
		double totalLength = 0.0;
		// Always do coArray.size() - 1 as the circular trick won't work
		// and we are using a i + 1 already
		for(int i = 0; i < (coArray.size() - 1); i++){
			totalLength += currentDistance(coArray.get(i), coArray.get(i+1));
		}
		// Last one and First one case
		XYC lastValue = coArray.get(coArray.size() - 1);
		XYC firstValue = coArray.get(0);
		totalLength += currentDistance(firstValue, lastValue);
		return totalLength;
	}
	
	public static void printCalculateTotalDistance(ArrayList<XYC> coArray){
		double totalLength = 0.0;
		// Always do coArray.size() - 1 as the circular trick won't work
		for(int i = 0; i < (coArray.size() - 1); i++){
			totalLength += currentDistance(coArray.get(i), coArray.get(i+1));
		}
		// Last one and First one case
		XYC lastValue = coArray.get(coArray.size() - 1);
		XYC firstValue = coArray.get(0);
		totalLength += currentDistance(firstValue, lastValue);
		System.out.printf("Length = %.4f\n\n", totalLength);
	}
	
	// Main Traveling Salesman Problem Function
	public static void TravellingSalesman(ArrayList<XYC> coArray){
		boolean loopTrue = true;
		// Print the Length of the iteration first
		printCalculateTotalDistance(coArray);
		double initialDistance = calculateTotalDistance(coArray);
		// Actual Calculations for Steps now
		Map<int[], Double> allPossibleValues = new HashMap<int[], Double>();
		double lastDecrease = 0.0;
		while(loopTrue){
			//  for each pair of points U,V, calculate the change to the length
	        // caused by swapping U and V;
			for(int i = 0; i < coArray.size(); i++){
				for(int j = 0; j < coArray.size(); j++){
					// Swap U, V
					swap(coArray, i, j);
					int[] currentIndex = new int[2]; 
					currentIndex[0] = i; currentIndex[1] = j;
					double calculatedTotal = calculateTotalDistance(coArray);
					allPossibleValues.put(currentIndex, calculatedTotal);
					swap(coArray, j, i);
				}
			}
			int[] largestChangeArray = new int[2];
			double largestDecrease = -100;
			for (int[] key : allPossibleValues.keySet()){
				if(initialDistance - allPossibleValues.get(key) > largestDecrease){
					if(initialDistance - allPossibleValues.get(key) != lastDecrease){
						largestChangeArray = key;
						largestDecrease = initialDistance - allPossibleValues.get(key);
					}
				}
			}
			if(lastDecrease > largestDecrease){
				loopTrue = false;
				break;
			} else {
				lastDecrease = largestDecrease;
				// Make Swap Deceleration
				if(largestChangeArray[0] < largestChangeArray[1]){
					System.out.printf("Swap %d and %d\n\n", largestChangeArray[0] + 1, largestChangeArray[1] + 1);
				} else {
					System.out.printf("Swap %d and %d\n\n", largestChangeArray[1] + 1, largestChangeArray[0] + 1);
				}
				// Swap
				swap(coArray, largestChangeArray[0], largestChangeArray[1]);
				// Print the Length right now
				// Print all X then Y
				// PRINT X First
				for(int i = 0; i < coArray.size(); i++){
					if(i < coArray.size() - 1){System.out.print(coArray.get(i).getX() + " ");}
					else{System.out.println(coArray.get(i).getX());}
				}
				// PRINT X
				for(int i = 0; i < coArray.size(); i++){
					if(i < coArray.size() - 1){System.out.print(coArray.get(i).getY() + " ");}
					else{System.out.println(coArray.get(i).getY());}
				}
				System.out.println();
				printCalculateTotalDistance(coArray);
			}
		}
	}
	
	// Printing the final output & parsing section
	public static void main(String[] args) throws IOException {
		// Coordinates Array
		ArrayList<XYC> coArray = new ArrayList<XYC>();
		// Parsing Information
		BufferedReader stdin = new BufferedReader(new InputStreamReader((System.in)));
		String row1 = stdin.readLine();
		StringTokenizer XCordinates = new StringTokenizer(row1);
		StringTokenizer XCordinatesPrint = new StringTokenizer(row1);
		String row2 = stdin.readLine();
		StringTokenizer YCordinates = new StringTokenizer(row2);
		StringTokenizer YCordinatesPrint = new StringTokenizer(row2);
		// Getting Information
		int tokensCurrent = XCordinates.countTokens();
		System.out.println("Path: ");
		// Print First, Print XCordinates
		for(int i = 0; i < tokensCurrent; i++){
			if(i < (tokensCurrent - 1)){System.out.print(XCordinatesPrint.nextToken() + " ");}
			else{System.out.println(XCordinatesPrint.nextToken());}
		}
		// Print YCordinates
		for(int i = 0; i < tokensCurrent; i++){
			if(i < (tokensCurrent - 1)){System.out.print(YCordinatesPrint.nextToken() + " ");}
			else{System.out.println(YCordinatesPrint.nextToken());}
		}
		// Print Empty Line as shown
		System.out.println();
		// Tackle the input
		for(int i = 0; i < tokensCurrent; i++){
			float X = Float.parseFloat(XCordinates.nextToken());
			float Y = Float.parseFloat(YCordinates.nextToken());
			XYC current = new XYC(X, Y);
			coArray.add(current);
		}
		// Tackle the problem that it is circular
		/*if(coArray.get(0) != null){
			float XofFirst = coArray.get(0).getX();
			float YofFirst = coArray.get(0).getY();	
			XYC repair = new XYC(XofFirst, YofFirst);
			coArray.add(repair);
		}
		*/
		TravellingSalesman(coArray);
		System.out.println("End of hill climbing");
	}
}
