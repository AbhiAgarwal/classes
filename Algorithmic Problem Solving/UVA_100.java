/*
 * The 3n + 1 problem
 * Time Limit:3000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
public class UVA_100 {
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String message;
		StringTokenizer st;
		while((message = stdin.readLine()) != null){
			st = new StringTokenizer(message);
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			System.out.print(a + " ");
			System.out.print(b + " ");
			if(a > b){
				int temp = a;
				a = b;
				b = temp;
			}
			long maximum = 0;
			for (long x = a; x <= b; x++){
				long cycleCountValue = cycleCount((x));
				if(cycleCountValue > maximum){
					maximum = cycleCountValue;
				}
			}
			System.out.print(maximum);
			System.out.println("");
		}
	}
	public static long cycleCount(long n){
		long length = 1;
		while(n != 1){
			n = n % 2 == 0 ? (n/2) : (3*n + 1);
			length++;
		}
		return length; 
	}
}