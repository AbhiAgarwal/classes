
public class UVA_11475 {
	
	public static int[] buildKMPTable(String pattern)
	{
		int[] table = new int[pattern.length()+1];
		for (int i = 2; i < table.length; ++i)
		{
			int j = table[i-1];
			do
			{
				if (pattern.charAt(j) == pattern.charAt(i-1)) { table[i] = j+1; break;}
				else j = table[j];
			} while (j != 0);
		}
		return table;
	}
	/** Returns the final state when simulating the DFA built using pattern on the string text */
	public static int simulate(int[] table, String pattern, String text)
	{
		int state = 0;
		for (int i = 0; i < pattern.length(); ++i)
		{
			while (true)
			{
				if (pattern.charAt(i) == text.charAt(state)) { state++; break; }
				else if (state == 0) break;
				state = table[state];
			} 
			if (state == table.length -1) break;
		}
		return state;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
