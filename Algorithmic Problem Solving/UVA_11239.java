/*
 * B - Open Source
 * Time Limit:3000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class UVA_11239 {
	
	public static String maxValue(Map<String, Integer> toSort){
		int maxValueInMap = (Collections.max(toSort.values())); 
		String value = null;
		if(!toSort.isEmpty()){
			for (Entry<String, Integer> entry : toSort.entrySet()) {
	            if (entry.getValue()==maxValueInMap) {
	            	value = entry.getKey();
	            }
	        }
			return value;
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String message = null;
		Map<String, Integer> toSort = new HashMap<String, Integer>();
		Map<String, ArrayList<String>> toIndividuals = new HashMap<String, ArrayList<String>>();
		ArrayList<String> allIndividuals = new ArrayList<String>();
		String initial = null;
		StringBuilder build = new StringBuilder();
		while ((message = stdin.readLine()) != null){
			if(message.equals("0")){break;}
			if(!message.equals("1")){
				if(Character.isUpperCase(message.charAt(0))){
					initial = message;
					toSort.put(initial, 0);
					toIndividuals.put(initial, new ArrayList<String>());
				} else {
					toSort.put(initial, toSort.get(initial) + 1);
					ArrayList<String> addition = toIndividuals.get(initial);
					addition.add(message);
					toIndividuals.put(initial, addition);
				}
			}
		}
		System.out.println(toIndividuals);
		String currentPosition;
		while(!toSort.isEmpty() && (currentPosition = maxValue(toSort)) != null){
			int value = toSort.get(currentPosition);
			toSort.remove(currentPosition);
		}
	}
}

class ValueComparator implements Comparator<String> {
    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}