/*
 * I Can Guess The Data Structure
 * Time Limit:1000MS     
 * Memory Limit:0KB    
 * 64bit IO Format:%lld & %llu
 */

import java.io.*;
import java.util.*;

public class UVA_11995 {
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder build = new StringBuilder();
		StringTokenizer st;
		String message;
		Queue<Integer> queue;
		boolean queue_q = true;
		Stack<Integer> stack;
		boolean stack_q = true;
		PriorityQueue<Integer> priorityqueue;
		boolean priorityqueue_q = true;
		while((message = stdin.readLine()) != null){
			int value = Integer.parseInt(message);
			queue = new LinkedList<Integer>();
			stack = new Stack<Integer>();
			priorityqueue = new PriorityQueue<Integer>(1000, Collections.reverseOrder());
			queue_q = true;
			stack_q = true;
			priorityqueue_q = true;
			while(value-- > 0){ // decrementing value
				message = stdin.readLine();
				st = new StringTokenizer(message);
				int commandInput = Integer.parseInt(st.nextToken());
				int inputValue = Integer.parseInt(st.nextToken());
				if(commandInput == 1){ // removing
					queue.add(inputValue);
					stack.add(inputValue);
					priorityqueue.add(inputValue);
				} else {
					// check if not same value
					if(stack.empty() || (stack_q && stack.pop() != inputValue)){stack_q = false;} 
					if(queue.isEmpty() || (queue_q && queue.poll() != inputValue)){queue_q = false;}
					if(priorityqueue.isEmpty() || (priorityqueue_q && priorityqueue.poll() != inputValue)){priorityqueue_q = false;}
				}
			}
			if((queue_q && stack_q) || (stack_q && priorityqueue_q) || (priorityqueue_q && queue_q)){build.append("not sure\n");}
			else if(!queue_q && !stack_q && !priorityqueue_q){build.append("impossible\n");}
			else if(queue_q && !stack_q && !priorityqueue_q){build.append("queue\n");}
			else if(stack_q && !queue_q && !priorityqueue_q){build.append("stack\n");}
			else if(priorityqueue_q && !queue_q && !stack_q){build.append("priority queue\n");}
		}
		System.out.print(build);
	}
}