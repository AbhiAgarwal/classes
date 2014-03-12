/*
 * Maximum Sub-sequence Product
 * Time Limit:3000MS     
 * Memory Limit:0KB     
 * 64bit IO Format:%lld & %llu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class UVA_787 {
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String message;
		StringTokenizer st;
		while ((message = stdin.readLine()) != null){
			ArrayList<Integer> currentSection = new ArrayList<Integer>(200);
			st = new StringTokenizer(message);
			int currentNumber;
			while ((currentNumber = Integer.parseInt(st.nextToken())) != -999999) {
				currentSection.add(currentNumber);
			}
			BigInteger maximumProduct = BigInteger.valueOf(-1000000);
			BigInteger currentProduct = BigInteger.valueOf(1);
			for(int i = 0; i < currentSection.size(); i++) {
				currentProduct = BigInteger.valueOf(1);
                for(int x = i; x < currentSection.size(); x++) {
                	BigInteger current_val = BigInteger.valueOf(currentSection.get(x));
                	currentProduct = currentProduct.multiply(current_val);
                    if(currentProduct.compareTo(maximumProduct) == 1){
                    	maximumProduct = currentProduct;
                    }
                }
            }
			System.out.println(maximumProduct.toString());
		}
	}	
}
