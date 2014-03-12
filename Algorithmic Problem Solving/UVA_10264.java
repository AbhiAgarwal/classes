import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * The Most Potent Corner
 * Time Limit:3000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

public class UVA_10264 {
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder build = new StringBuilder();
		String message; int N;
		// Arbitrarily have an upper bound to the number of edges
		int largestSum = (int) Math.pow(2, 15);
		int[] allEdges = new int[largestSum], sumOfWeights = new int[largestSum];
		while ((message = stdin.readLine()) != null){
			N = Integer.parseInt(message);
			int currentSum = 0; int maximumPossibleEdges = (int) Math.pow(2, N);
			for(int i = 0; i < maximumPossibleEdges; i++){
				// inserting each string into the allEdges array
				message = stdin.readLine(); allEdges[i] = Integer.parseInt(message);
			}
			// calculation going through each edge
			for(int i = 0; i < maximumPossibleEdges; i++){
				int currentEdgeWeight = 0;
				// Goes through accounting for the dimension
				for(int x = 0; x < N; x++){
					// calculation for the current edge
					int iterateValue = i ^ (1 << x);
					// using the shift (ie:(0,0,1)) to access value
					currentEdgeWeight += allEdges[iterateValue];
				}
				sumOfWeights[i] = currentEdgeWeight;
			}
			for(int i = 0; i < maximumPossibleEdges; i++){
				for(int x = 0; x < N; x++){
					// calculation to access the array
					int iterateValue = i ^ (1 << x);
					//index is i ^ (1 << x). XOR, not Math.pow
					int largerThanOrNot = sumOfWeights[i] + sumOfWeights[iterateValue];
					if(currentSum < largerThanOrNot){
						currentSum = largerThanOrNot;
					}
				}
			}
			build.append(currentSum + "\n");
		}
		System.out.print(build);
	}
}
