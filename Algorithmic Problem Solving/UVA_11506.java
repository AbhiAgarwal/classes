import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;
import java.util.StringTokenizer;

/*
 * Angry Programmer
 * Time Limit:4000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
*/

public class UVA_11506 {
	// get number from hash
	static int getNum(HashMap<String, Integer> map, String s)
	{
		if (!map.containsKey(s)) {map.put(s, map.size());}
		return map.get(s);
	}
	// Parse & Start into process(...)
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder build = new StringBuilder();
		StringTokenizer st = new StringTokenizer(stdin.readLine());
		// A line with two integers M and W representing (respectively) the 
		// M = number of machines and the W = number of wires in the network.
		int M = 0, W = 0;
		// Parsing was EFFING hard and sending it into process() is kinda pointless..
		// so I'm gonna merge them together
		while((M = Integer.parseInt(st.nextToken())) != 0 && (W = Integer.parseInt(st.nextToken())) != 0){
			// Input into process(...) array
			ArrayList<ArrayList<Integer>> input = new ArrayList<ArrayList<Integer>>();
			HashMap<String, Integer> machineMap = new HashMap<String,Integer>();
			
			// M - 2 lines, one per machine (different from the boss' machine 
			// and the central server), containing the following information 
			// separated by spaces
			for(int i = 0; i < (M - 1); i++){
				st = new StringTokenizer(stdin.readLine());
				// we do 0-index
				int machineIdentificiation = Integer.parseInt(st.nextToken()) - 1;
				int machineCost = Integer.parseInt(st.nextToken());
			}
			
			// W lines, one per wire, containing the following information 
			// separated by spaces
			for(int i = 0; i < W; i++){
				st = new StringTokenizer(stdin.readLine());
				// specifying the identifiers of the machines linked by the wire
				int identifiersOfTheMachinesI = Integer.parseInt(st.nextToken());
				int identifiersOfTheMachinesJ = Integer.parseInt(st.nextToken());
				// An integer d specifying the cost of cutting the wire
				int identifiersOfTheMachinesD = Integer.parseInt(st.nextToken());
			}
			
		}
	}
	// Adds an Edge
	static void addEdge(int u, int v, int cap, int[][] caps, ArrayList<Integer>[] adj)
	{
		if (caps[u][v] == 0 && caps[v][u] == 0)
		{
			adj[u].add(v); 
			adj[v].add(u);
		} 
		caps[u][v] += cap;
	}
	
	// Starts the process
	// static void process(ArrayList<ArrayList<Integer>> input){}
	
	// Max Flow through Each Process
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
	
	// Starts An Augment
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
	
	// Updates from each Augment
	static void update(int[][] caps, int[] pred, int curr, int f)
	{
		int p = pred[curr];
		if (p == curr) return;
		caps[p][curr] -= f;  caps[curr][p] += f;
		update(caps,pred,p,f);
	}
}