import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;

/*
- Eventually Periodic Sequence
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Parsing sucks with C++BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
*/

public class UVA_11036 {
	
	public static long f(ArrayList<String> currentLine, long N, long n){
		// currentLine.size() - 1 meaning no % sign
		Stack<Long> inputStack = new Stack<Long>();
		for(int i = 0; i < (currentLine.size()); i++){			
			String currentWord = currentLine.get(i);
			// Cases are:
			/*
			 * 10 1 x N %
			 * 11 1 x x 1 + * N %
			 * 1728 1 x x 1 + * x 2 + * N %
			 * 1728 1 x x 1 + x 2 + * * N %
			 * 100003 1 x x 123 + * x 12345 + * N %
			 */
			if(currentWord.equals("x")){
			// If it is x then push the small n value
				inputStack.push(Long.valueOf(n));
			} else if(currentWord.equals("*")){
					// If it is multiply then check case before/after
					inputStack.push(Long.valueOf((inputStack.pop() * inputStack.pop()) % N));
			} else if(currentWord.equals("+")){
					// Add them
					inputStack.push(Long.valueOf((inputStack.pop() + inputStack.pop()) % N));
			} else if(currentWord.equals("N")){
					// If it is N
					inputStack.push(N);
			} else if(currentWord.equals("%")){
				// Pop values are totally different here
				// Because we are doing pop2 % pop1
				long pop1 = inputStack.pop();
				long pop2 = inputStack.pop();
				inputStack.push(Long.valueOf(pop2 % pop1));
			} else {
					// Now they are values such as 123
					inputStack.push(Long.parseLong((currentWord)));
			}
		}
		return inputStack.pop();
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder build = new StringBuilder();
		String message;
		while ((message = stdin.readLine()) != null){
			// Getting Values
			StringTokenizer st = new StringTokenizer(message);
			ArrayList<String> currentLine = new ArrayList<String>();
			// Our visit HASHMAP
			// Did not WORK without HASHMAP -> too slow?
			HashMap<Long, Boolean> visited = new HashMap<Long, Boolean>();
			
			// Initializing
			long N = Long.parseLong(st.nextToken());
			long n = Long.parseLong(st.nextToken());
			if(N == 0){break;}
			while(st.countTokens() > 0){
				currentLine.add(st.nextToken());
			}
			// Return Answer
			long toAnswer = 0;
			// We have n and f(x) (the first one)
			// n is the first argument we use
			// n = f(x) here
			visited.put(n, true);
			// This will loop through many f(f(f(f(f infinity values
			// Until we find it
			while(true){
				n = f(currentLine, N, n);
				// If our HASH DOES EXIST
				// IF IT does exist we have done our job and so we break
				if(visited.get(n) != null){
					break;
				} else {
					// If it exists then we return TRUE
					// we have visited it
					visited.put(n, true);
				}
			}
			// While this exists
			while(visited.get(n)){
				// We set it to false so we don't come back to it
				visited.put(n, false);
				n = f(currentLine, N, n);
				toAnswer++;
			}
			
			// Build Answer
			build.append(toAnswer).append("\n");
		}
		System.out.print(build.toString());
	}
}
