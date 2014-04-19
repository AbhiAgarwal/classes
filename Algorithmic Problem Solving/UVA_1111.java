/*
 * Trash Removal
 * Time Limit:15000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
*/

// http://cs.nyu.edu/courses/spring14/CSCI-UA.0480-004/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class UVA_1111 {
	
	public static class Geometry
	{
		static final double EPS = 1e-9;
		static class Point
		{
			public double x, y;
			public Point(double x, double y) { this.x = x; this.y = y; }
			public String toString() { return String.format("%.3f %.3f\n", x,y); }
		}	
		/** Computes the unsigned angle abc */
		static double angle(Point a, Point b, Point c)
		{
			double d1 = dist(a,b), d2 = dist(b,c);
			if (d1 < EPS || d2 < EPS) return 0;
			return Math.acos( ((a.x-b.x)*(c.x-b.x)+(a.y-b.y)*(c.y-b.y))/(d1*d2) );
		}
		/** Uses winding numbers to test for inside */
		static boolean inside(ArrayList<Point> al, Point p)
		{
			int n = al.size();
			if (n == 0) return false;
			double ret = 0;
			for (int i = 0; i < n; ++i)
			{
				int ii = (i+1)%n;	
				if (isCollin(al.get(i), p, al.get(ii))) return true;
				ret += (isLeft(al.get(i),p,al.get(ii))?-1:1) * angle(al.get(i),p,al.get(ii));
			}
			return Math.abs(ret)>EPS;
		}
		/** Cross product (b-a) x (c-b) */
		static double cross(Point a, Point b, Point c)
		{
			double y1 = b.y-a.y, y2 = c.y-b.y;
			double x1 = b.x-a.x, x2 = c.x-b.x;
			return x1*y2-x2*y1;
		}
		/** Distance from c to the line through a,b */
		static double height(Point a, Point b, Point c) { return Math.abs(cross(a,b,c))/dist(a,b); }
		/** Distance from a to b */
		static double dist(Point a, Point b) { return Math.hypot(a.x-b.x,a.y-b.y); }
		static double dist2(Point a, Point b, double pointSlope){ 
			return Math.hypot(a.x-((b.y-a.y)/(pointSlope+a.x)),a.y-(b.y)); 
		}
		/** Area of the polygon given in al */
		static double area(ArrayList<Point> al)
		{
			Point p = new Point(0,0);
			int N = al.size();
			double ret = 0;
			for (int i = 0; i < N; ++i)
			{
				int ii = (i+1)%N;
				ret += cross(p,al.get(i),al.get(ii));
			}
			return Math.abs(ret);			
		}
		/** Perimeter of the polygon given in al */
		static double perimeter(ArrayList<Point> al)
		{
			int N = al.size();
			double ret = 0;
			for (int i = 0; i < N; ++i)
			{
				int ii = (i+1)%N;
				ret += dist(al.get(i),al.get(ii));
			}
			return ret;
		}
		/** Returns if a->b->c forms a left turn */
		static boolean isLeft(Point a, Point b, Point c) { return cross(a,b,c) >= 0; }
		/** Returns if a,b,c are collinear*/
		static boolean isCollin(Point a, Point b, Point c) { return Math.abs(cross(a,b,c)) < EPS; }
		/** Comparator used to sort for Graham Scan*/
		static class PivotComp implements Comparator<Point>
		{
			Point p;
			public PivotComp(Point p) { this.p = p; }
			@Override
			public int compare(Point o1, Point o2)
			{
				double d1 = Math.hypot(o1.y-p.y, o1.x-p.x);
				double d2 = Math.hypot(o2.y-p.y, o2.x-p.x);
				if (isCollin(p,o1,o2)) return d1 < d2 ? -1 : d1 > d2 ? 1 : 0;
				double a1 = Math.atan2(o1.y-p.y, o1.x-p.x);
				double a2 = Math.atan2(o2.y-p.y, o2.x-p.x);
				return a1 < a2 ? -1 : a1 > a2 ? 1 : 0;
			}
		}
		/** SLOPE */
		static double slope(Point A, Point B){
			return (B.y - A.y)/(B.x - A.x);
		}
		
		/** Convex hull */
		static ArrayList<Point> chull(ArrayList<Point> al)
		{
			ArrayList<Point> stack = new ArrayList<Point>();
			if (al.size() <= 3)
			{
				stack.addAll(al);
				return stack;
			}
			int best = 0;
			for (int i = 1; i < al.size(); ++i)
				if (al.get(i).y < al.get(best).y || Math.abs(al.get(i).y-al.get(best).y) < EPS && al.get(i).x < al.get(best).x) best = i;
			Point tmp = al.get(0); al.set(0, al.get(best)); al.set(best, tmp);
			tmp = al.remove(0);
			Collections.sort(al,new PivotComp(tmp));
			al.add(0,tmp);
			stack.add(al.get(0)); stack.add(al.get(1));
			for (int i = 2; i < al.size(); ++i)
			{
				stack.add(al.get(i));
				while (stack.size() > 2)
				{
					int S = stack.size();
					if (isLeft(stack.get(S-3),stack.get(S-2),stack.get(S-1))) break;
					else stack.remove(S-2);
				}
			}
			return stack;
		}
	}
	
	public static void main(String[] args) throws IOException {
		// The input contains several test cases
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer tokenizer = new StringTokenizer(in.readLine()); StringBuilder builder = new StringBuilder();
		int n = Integer.parseInt(tokenizer.nextToken()), caseNumber = 1;
		double finalAnswer = 0.00;
		while(n > 0){ /* n (3 <= n <= 100), initialize: */
			ArrayList<Geometry.Point> allPoints = new ArrayList<Geometry.Point>(); finalAnswer = 0.00;
			/* Parse Input */
			for(int i = 0; i < n; ++i){ tokenizer = new StringTokenizer(in.readLine());
				allPoints.add(new Geometry.Point(Integer.parseInt(tokenizer.nextToken()), Integer.parseInt(tokenizer.nextToken())));
			}
			
			/* Run Convex Hull*/ ArrayList<Geometry.Point> convex = Geometry.chull(allPoints);
			// Use an O(n^2) algorithm chute width considering all sides of the convex hull and all possible opposing points
			
			/* PROCESS */
			for(int i = 0; i < convex.size() - 1; ++i){

				for(int x = 0; x < convex.size(); ++x){
					
				}
			}	
			
			/* ANSWER */ builder.append(String.format("Case %d: %.2f\n", caseNumber, finalAnswer));
			/* Get Ready for next input */ tokenizer = new StringTokenizer(in.readLine()); n = Integer.parseInt(tokenizer.nextToken()); caseNumber++;
		}
		System.out.printf(builder.toString());
	}
}