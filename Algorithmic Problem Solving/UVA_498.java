/*
 * Polly the Polynomial
 * Time Limit:3000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UVA_498 {
	static int pow(int x, int y) {
		if(y == 0){return 1;} 
		if(y == 1){return x;}
		if(y % 2 == 0){ return pow(x*x, y/2);
		} else {return x * pow(x*x, (y-1)/2);}
	}
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder build = new StringBuilder();
		String message;
		while ((message = stdin.readLine()) != null) {
			String[] lineOne = message.split("[ ]+");
			String[] lineTwo = stdin.readLine().split("[ ]+");
			int value = lineOne.length - 1;
			int[] valueArray = new int[value + 1];
			for (int i = 0; i <= value; ++i){
				valueArray[i] = Integer.parseInt(lineOne[i]);
			}
			for (int i = 0; i < lineTwo.length; ++i) {
				int x = Integer.parseInt(lineTwo[i]);
				int y = 0;
				for (int j = value; j >= 0; --j) {
					int values = valueArray[value - j] * pow(x, j);
					y += values;
				}
				if (i > 0){build.append(" ");}
				build.append(y);
			}
			build.append("\n");
		}
		System.out.print(build);
	}
}