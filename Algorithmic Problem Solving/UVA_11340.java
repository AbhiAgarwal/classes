/*
 * Newspaper
 * Time Limit:1000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

import java.io.*;
import java.util.*;

public class UVA_11340 {
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader((System.in)));
		int numberOfTests = Integer.parseInt(stdin.readLine());
		while (numberOfTests-- > 0) {
			Map<Character, Long> paidCharacters = new HashMap<Character, Long>();
			int numberOfPaid = Integer.parseInt(stdin.readLine());
			for(int p = 0; p < numberOfPaid; p++){
				String message = stdin.readLine().trim();
				StringTokenizer letterPrice = new StringTokenizer(message);
				paidCharacters.put(letterPrice.nextToken().charAt(0), Long.parseLong(letterPrice.nextToken()));
			}
			long newspaperValue = 0;
			int numberofLines = Integer.parseInt(stdin.readLine());
			for(int lines = 0; lines < numberofLines; lines++){
				String currentLine = stdin.readLine();
				for (char current : currentLine.toCharArray()){
					if(paidCharacters.containsKey(current)){
						newspaperValue += paidCharacters.get(current);
					}
				}
			}
			double finalValue = (double)newspaperValue;
			System.out.printf("%.2f$%n", (double)finalValue / 100.0);
		}
	}

}
