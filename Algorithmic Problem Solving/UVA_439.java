import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;


public class UVA_439 {
	
	// From book
	static int dr[] = {2, 2, -2, -2, 1, 1, -1, -1}; // E,N,W,S
	static int dc[] = {-1, 1, -1, 1, -2, 2, -2, 2}; // R,U,L,D

	public static class point {
		public int x;
		public int y;
		public int moves;
		point(int x, int y){
			this.x = x; this.y = y; this.moves = 0;
		}
	}
	
	private static boolean isInRange(point u) {
		if(!(u.x < 0) && !(u.x >= iterations) && !(u.y < 0) && !(u.y >= iterations)){ 
			return true; 
		} else { 
			return false; 
		}
	}
	
	static int iterations = 8;
	public static int bfs(point begin, point end){
	    /* EDGE CASE */
	    if(begin.x == end.x && begin.y == end.y){ return 0; }
	    /* RUN BFS */
	    boolean visited[][] = new boolean[iterations][iterations];
	    Queue<point> q = new LinkedList<point>(); q.add(begin); visited[begin.x][begin.y] = true;
	    while(!q.isEmpty()){
	        point u = q.peek(); q.poll();
	        if(u.x == end.x && u.y == end.y){ return u.moves; } // Reach the end
	        // All possible states
	        for(int i = 0; i < iterations; ++i){
	            point newPoint = new point(u.x + dr[i], u.y + dc[i]);
	            if(!isInRange(newPoint)){ continue; }
	            else if(visited[newPoint.x][newPoint.y]){ continue; }
	            else {
	                visited[newPoint.x][newPoint.y] = true;
	                newPoint.moves = u.moves + 1; q.add(newPoint);
	            }
	        }
	    }
	    return 0;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder build = new StringBuilder(); StringTokenizer stringToken;
		String currentLine, eachWordOne, eachWordTwo;
		int digitOne = 0, positionOne = 0, digitTwo = 0, positionTwo = 0;
		while ((currentLine = stdin.readLine()) != null) {
			stringToken = new StringTokenizer(currentLine);
			eachWordOne = stringToken.nextToken(); eachWordTwo = stringToken.nextToken();
			digitOne = eachWordOne.charAt(0) - 'a'; digitTwo = eachWordTwo.charAt(0) - 'a';
	        positionOne = eachWordOne.charAt(1) - '1'; positionTwo = eachWordTwo.charAt(1) - '1';
	        point begin = new point(digitOne, positionOne); point end = new point(digitTwo, positionTwo);    
	        build.append(String.format("To get from %s to %s takes %d knight moves.\n", eachWordOne, eachWordTwo, bfs(begin, end)));
		}
		System.out.print(build.toString());
	}
}
