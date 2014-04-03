/*
 * My T-shirt suits me
 * Time Limit:3000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;
import java.util.StringTokenizer;

public class UVA_11045 {
	// Correctly using the hash map to parse and map the values correctly
	// Our code removes 'rev' as we don't really need to reverse a string
	// We just need to print out the values YES OR NO.
	
	static int getNum(HashMap<String, Integer> map, String s)
	{
		if (!map.containsKey(s)) {map.put(s, map.size());}
		return map.get(s);
	}

	// Main The Main Iteration
	// This is initally just parsing the values 
	// And then putting them into an array and sending it off the process to execute upon
	// This just acts like the parses for N/M values.
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder build = new StringBuilder();
		StringTokenizer st = new StringTokenizer(stdin.readLine());
		// The first line of the input contains the number of test cases
		int T = Integer.parseInt(st.nextToken());
		// XXL, XL, L, M , S, or XS
		while(T-- > 0){
			// Make the HashMap local so we don't have to clear it each time
			HashMap<String, Integer> shirtMap = new HashMap<String,Integer>();
			// Initialization of Inputs
			st = new StringTokenizer(stdin.readLine());
			// N indicates the number of T-shirts
			int N = Integer.parseInt(st.nextToken());
			// Number M indicates the number of volunteers
			int M = Integer.parseInt(st.nextToken());
			// M lines are listed where each line contains, 
			// separated by one space, the two sizes that suit each volunteer
			ArrayList<ArrayList<Integer>> input = new ArrayList<ArrayList<Integer>>();
			// Go through number of Volunteers to get each shirt
			for(int i = 0; i < M; i++){
				// Get next Line
				st = new StringTokenizer(stdin.readLine());
				// Initialization of Array to put into ArrayLists
				ArrayList<Integer> al = new ArrayList<Integer>();
				// Parsing Values foreach Shirt
				String shirtOne = st.nextToken();
				String shirtTwo = st.nextToken();
				// shirt left
				al.add(getNum(shirtMap, shirtOne));
				// shirt right 
				al.add(getNum(shirtMap, shirtTwo));
				// Append it to the end for future uses
				input.add(al);
			}
			// For each test case you are to print a line containing 
			// YES if there is, at least, one distribution where T-shirts 
			// suit all volunteers, or NO, in other case.
			process(input, shirtMap, M, N);
		}
	}

	// Adds Each Edge to the Adjecency List
	static void addEdge(int u, int v, int cap, int[][] caps, ArrayList<Integer>[] adj)
	{
		if (caps[u][v] == 0 && caps[v][u] == 0)
		{
			adj[u].add(v); adj[v].add(u);
		} 
		caps[u][v] += cap;
	}

	// Runs each iteration of MAXFLOW
	// This is pretty much where the answer is released at
	static void process(ArrayList<ArrayList<Integer>> input, HashMap<String, Integer> shirtMap, int M, int shirtsWeHave)
	{
		// How many TV Shirts do we have right now
		// We can have an upper bound of 6 for N but it doesn't always have to be 6
		int N = shirtMap.size();
		// s = source, t = sink, sN = N 
		int s = 0, t = 1, sN = 2, sM = sN + N;
		// Size of the Total, it's just sink + source + N
		int V = 2 + N + M;

		// Initialization of Adjecency list
		ArrayList<Integer>[] adj = new ArrayList[V];
		for (int i = 0; i < V; ++i){
			adj[i] = new ArrayList<Integer>();
		}

		// Now we get each value from the Input & Parse it before we reach the other interesting things
		int[][] caps = new int[V][V];
		int x = sM;
		for (ArrayList<Integer> al : input){
			// Shirt One of each person, rightmost
			int n = al.get(0) + sN;
			// Shirt Two of each person, rightmost
			int p = al.get(1) + sN;
			// Join them as being ONE edge as they are of one person
			addEdge(n, x, 1, caps, adj);
			addEdge(p, x, 1, caps, adj);
			x++;
			// We don't require this section as this is for clubs
			//for (int i = 2; i < al.size(); ++i){
			//	addEdge(al.get(i) + sC, n, 1, caps, adj);
			//}
		}

		// We need to go through each p+sN value foreach N/6 as v as N/6 is the bound
		for (int p = 0; p < N; ++p){
			addEdge(s, p + sN, shirtsWeHave/6, caps, adj);
		}

		// Get to the real section now
		// Our lower code limit is N/6 
		for (int c = 0; c < M; ++c){
			addEdge(c + sM, t, 1, caps, adj);
		}

		// Calculate the max flow
		int flow = maxflow(adj, caps, s, t);

		// If the flow we have is less than than the number of volunteers
		// Then we print NO.
		if (flow < M){
			System.out.println("NO");
		} else {
			System.out.println("YES");
		}
	}

	// Runs the main iteration of max flow until the augment section breaks
	// When the augment section breaks it returns to the process loop where it goes through other iterations of MaxFlow
	static int maxflow(ArrayList<Integer>[] adj, int[][] caps, int source, int sink)
	{
		int ret = 0;
		while (true)
		{
			int f = augment(adj,caps,source,sink);
			if (f == 0) break;
			ret += f;
		}
		return ret;
	}

	// Goes through the different pipes & stuff.
	static int augment(ArrayList<Integer>[] adj, int[][] caps, int source, int sink)
	{
		Queue<Integer> q = new ArrayDeque<Integer>();
		int[] pred = new int[adj.length];
		Arrays.fill(pred,-1);
		int[] f = new int[adj.length];
		pred[source] = source; f[source] = Integer.MAX_VALUE; q.add(source);
		while (!q.isEmpty())
		{
			int curr = q.poll(), currf = f[curr];
			if (curr == sink)
			{
				update(caps,pred,curr,f[curr]);
				return f[curr];
			}
			for (int i = 0; i < adj[curr].size(); ++i)
			{
				int j = adj[curr].get(i);
				if (pred[j] != -1 || caps[curr][j] == 0) continue;
				pred[j] = curr; f[j] = Math.min(currf, caps[curr][j]); q.add(j);
			}
		}
		return 0;
	}

	// Updates the caps array each time when it runs
	static void update(int[][] caps, int[] pred, int curr, int f)
	{
		int p = pred[curr];
		if (p == curr) return;
		caps[p][curr] -= f;  caps[curr][p] += f;
		update(caps,pred,p,f);
	}
}