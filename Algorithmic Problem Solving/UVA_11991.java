/*
 * DONE IN C++
 * Easy Problem from Rujia Liu?
 * Time Limit:1000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class UVA_11991 {
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		String message;
		ArrayList<ArrayList<Integer>> mainArray = new ArrayList<ArrayList<Integer>>(1000000);
		while((message = stdin.readLine()) != null){
			st = new StringTokenizer(message);
			int n = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());
			message = stdin.readLine();
			st = new StringTokenizer(message);
			for(int i = 0; i < n; i++){
				int currentNumber = Integer.parseInt(st.nextToken());
				
				ArrayList<Integer> x = mainArray.get(currentNumber);
				System.out.print(x);
				x.add(n);
				mainArray.set(currentNumber, x);
			}
			System.out.print(mainArray);
			for(int i = 0; i < m; i++){
				message = stdin.readLine();
				st = new StringTokenizer(message);
				int k = Integer.parseInt(st.nextToken());
				int v = Integer.parseInt(st.nextToken());	
			}
			System.exit(0);
		}
	}
}