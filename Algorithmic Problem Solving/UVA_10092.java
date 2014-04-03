import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

/*
 * The Problem with the Problem Setter
 * Input: Standard Input
 * Output: Standard Output
*/
public class UVA_10092 {
	
	static StringBuilder sb = new StringBuilder();
	
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder build = new StringBuilder();
		StringTokenizer st = new StringTokenizer(stdin.readLine());
		// nk is the number of categories
		int Nk = 0;
		// np is the number of problems in the pool
		int Np = 0;
		while((Nk = Integer.parseInt(st.nextToken())) != 0 && (Np = Integer.parseInt(st.nextToken())) != 0){
			st = new StringTokenizer(stdin.readLine());
			// The second line contains nk positive integers where the i-th 
			// integer specifies the number of problems to be included in category 
			// i (1 <= i <= nk) of the test
			// nk integers will never exceed 100.
			ArrayList<Integer> categoryInformation = new ArrayList<Integer>(Nk);
			for(int i = 0; i < Nk; i++){
				categoryInformation.add(Integer.parseInt(st.nextToken()));
			}
			// j-th (1 <= j <= np) of the next np lines contains the category 
			// information of the j-th problem in the pool
			ArrayList<ArrayList<Integer>> category = new ArrayList<ArrayList<Integer>>(Np);
			
			// Gets the next Np lines of input for each j-th problemset in the pool.
			for(int j = 0; j < Np; j++){
				// Get Each Line
				st = new StringTokenizer(stdin.readLine());
				// Parse Current
				int categorySize = Integer.parseInt(st.nextToken());
				ArrayList<Integer> current = new ArrayList<Integer>(categorySize);
				// Parse each line, put it into ArrayList and then add it into category
				for(int i = 0; i < categorySize; i++){
					current.add(Integer.parseInt(st.nextToken()));
				}
				// Add to the big ArrayList
				category.add(current);
				// System.out.print(current + "\n");
			}
			// Sending it off to Max Flow & To get Output'ed
			process(category, categoryInformation);
			// Next Line
			st = new StringTokenizer(stdin.readLine());
		}
		System.out.print(sb.toString());
	}
	
	// Adds each Edge
	static void addEdge(int u, int v, int cap, int[][] caps, ArrayList<Integer>[] adj)
	{
		if (caps[u][v] == 0 && caps[v][u] == 0)
		{
			adj[u].add(v); adj[v].add(u);
		} 
		caps[u][v] += cap;
	}
	
	private static StringBuilder runPrinting(ArrayList<Integer>[] adj, int[][] caps, ArrayList<ArrayList<Integer>> input, ArrayList<Integer> categoryInformation, int sCat, int t){
		StringBuilder sbCurrent = new StringBuilder();
		// Go through all iterations through the 2-D array
		for (int i = sCat; i < (sCat + categoryInformation.size()); ++i){
			// So we can have whitespaces
			boolean notFirst = false;
			for(int x = 0; x < adj[i].size(); ++x){
				// get current indexed number (ALL THE NUMBERS)
				int currentNumber = adj[i].get(x);
				if(currentNumber < t && currentNumber > categoryInformation.size() && caps[currentNumber][i] > 0){
					// If the number is not first then we want to seperate it
					if(notFirst){
						sbCurrent.append(" ");
					} else {
						notFirst = true;
					}
					// lines contains the problem numbers that can be included in category i
					sbCurrent.append(currentNumber - categoryInformation.size());
				}
			}
			sbCurrent.append("\n");
		}
		// return StringBuilder
		return sbCurrent;
	}
	
	// Runs through each process, which executes MaxFlow
	static void process(ArrayList<ArrayList<Integer>> input, ArrayList<Integer> categoryInformation)
	{
		// S is SOURCE
		// T is SINK
		int s = 0;
		int sCat = 1;
		int sInp = sCat + categoryInformation.size();
		// sInp is sCat + size of category information
		// Directly 
		int t = 1 + categoryInformation.size() + input.size();
		// catSize is size of the categories (categoryInformation.size()) after the sink
		//int catSize = 2;
		// inpSize is the size of the input categoryies after the catSize
		// int inpSize = catSize + categoryInformation.size();
		// V is the total size we have to declare
		int V = 2 + input.size() + categoryInformation.size();
		// Initialization
		int[][] caps = new int[V][V];
		ArrayList<Integer>[] adj = new ArrayList[V];
		for (int i = 0; i < V; ++i){
			adj[i] = new ArrayList<Integer>();
		}
		
		int finalAnswerComparison = 0;
		// source -------> al.get --------> i ---------> sink it	
		// catSize1 + ... + catSizei connect to t (1)
		// So catSize + i --------> t
		// Parsing Values to format for maxflow and Adjacency Array
		// Each Category here to the Sink
		for (int i = sCat; i < (sCat + categoryInformation.size()); ++i){
			// Wait is adjusted as we already took it from the ArrayList
			// We have a special ArrayList called categoryInformation
			// with category information of the j-th problem in the pool
			addEdge(s, i, categoryInformation.get(i - sCat), caps, adj);
			finalAnswerComparison += categoryInformation.get(i - sCat);
		}
		// Now we parse through the input ArrayList<ArrayList<Integer>>
		// We need "i" so lets not use for (ArrayList<Integer> al : input)
		// category ->>>>>>> sink
		for(int i = sInp; i < (sInp + input.size()); ++i){
			ArrayList<Integer> al = input.get(i - sInp);
			// We use inpSize this time
			// i -------- > sink
			addEdge(i, t, 1, caps, adj);
			for(int x = 0; x < al.size(); x++){
				// al.get value --------> i
				addEdge(al.get(x), i, 1, caps, adj);
			}
		}
		// Run maxFlow
		int flow = maxflow(adj, caps, s, t);
		// Run Backtracking algorithm here
		// For each test case in the input print a line containing either 1 or 0 
		// depending on whether or not problems can be successfully selected form 
		// the pool under the given restrictions (1 for success and 0 for failure).
		if(flow < finalAnswerComparison){
			sb.append(0);
			sb.append('\n');
		} else {
			sb.append(1);
			sb.append('\n');
			sb.append(runPrinting(adj, caps, input, categoryInformation, sCat, t).toString());
		}
	}

	// Max Flow Running
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
	
	// Augment running via Max Flow with Adjacency List
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
	
	// Update Each Caps 2-D Array
	static void update(int[][] caps, int[] pred, int curr, int f)
	{
		int p = pred[curr];
		if (p == curr) return;
		caps[p][curr] -= f;  caps[curr][p] += f;
		update(caps,pred,p,f);
	}
}
