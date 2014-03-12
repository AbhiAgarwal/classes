/*
 * C - Virtual Friends
 * Time Limit:10000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class UVA_11503 {
	static int[] union_find;
	static int[] union_size;
	public static int find(int a){
		if(union_find[a] == a){return a;}
		return find(union_find[a]);
	}
	public static boolean same(int a, int b){
		return (find(a) == find(b));
	}
	public static void union(int a, int b){
		if(!same(a, b)){
			union_size[find(b)] += union_size[find(a)];
			union_find[find(a)] = find(b);
		}
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader((System.in)));
		StringBuilder build = new StringBuilder();
		StringTokenizer st;
		union_find = new int[100000]; union_size = new int[100000];
		int numberOfTests = Integer.parseInt(stdin.readLine());
		while (numberOfTests-- > 0) {
			int testCases = Integer.parseInt(stdin.readLine());
			for(int p = 0; p <= testCases; p++){
				union_find[p] = p; union_size[p] = 1;
			}
			int currentPositionOfElement = 0;
			Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
			for(int m = 0; m < testCases; m++){
				st = new StringTokenizer(stdin.readLine());
				int valuesOne, valuesTwo;
				String firstHalf = st.nextToken();
				String secondHalf = st.nextToken();
				if(hash.containsKey(firstHalf)){valuesOne = hash.get(firstHalf);}
				else{valuesOne = currentPositionOfElement;
					hash.put(firstHalf, currentPositionOfElement);
					currentPositionOfElement += 1;}
				if(hash.containsKey(secondHalf)){valuesTwo = hash.get(secondHalf);}
				else{valuesTwo = currentPositionOfElement;
					hash.put(secondHalf, currentPositionOfElement);
					currentPositionOfElement += 1;}
				union(valuesOne, valuesTwo);
				build.append(union_size[find(valuesOne)] + "\n");//indexposition
			}
		}
		System.out.print(build);
	}
}