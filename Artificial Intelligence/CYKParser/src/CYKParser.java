import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CYKParser {
	// Initialization for the P[][][] Array
	public static void setPToFalse(int N, int M, Tree P[][][]){
		for(int i = 0; i < M; i++){
			for(int x = 0; x < N; x++){
				for(int y = 0; y < N; y++){
					P[i][x][y] = null;
				}
			}
		}
	}
	// Convert from String to NonTerm
	public static NonTerm getNonTerm(String NonTermObject){
		if(NonTermObject == "Noun"){
			return NonTerm.Noun;
		} else if(NonTermObject == "S"){
			return NonTerm.S;
		} else if(NonTermObject == "NP"){
			return NonTerm.NP;
		} else if(NonTermObject == "PP"){
			return NonTerm.PP;
		} else if(NonTermObject == "PPList"){
			return NonTerm.PPList;
		} else if(NonTermObject == "Prep"){
			return NonTerm.Prep;
		} else if(NonTermObject == "Verb"){
			return NonTerm.Verb;
		} else if(NonTermObject == "VerbAndObject"){
			return NonTerm.VerbAndObject;
		} else if(NonTermObject == "VPWithPPList"){
			return NonTerm.VPWithPPList;
		} else{
			return null;
		}
	}
	// Get NonTerm Number
	public static int getNonTermNumber(String NonTermObject){
		if(NonTermObject == "Noun"){
			return 0;
		} else if(NonTermObject == "S"){
			return 1;
		} else if(NonTermObject == "NP"){
			return 2;
		} else if(NonTermObject == "PP"){
			return 3;
		} else if(NonTermObject == "PPList"){
			return 4;
		} else if(NonTermObject == "Prep"){
			return 5;
		} else if(NonTermObject == "Verb"){
			return 6;
		} else if(NonTermObject == "VerbAndObject"){
			return 7;
		} else if(NonTermObject == "VPWithPPList"){
			return 8;
		} else{
			return -1;
		}
	}
	public static Tree[][][] CYKParse(String sentence, ArrayList<String[]> grammar){
		StringTokenizer st;
		st = new StringTokenizer(sentence); // for the next loop
		int N = st.countTokens();
		int M = grammar.size();
		Tree P[][][] = new Tree[M][N][N]; // P[i, x, y]
		// Initialization of the Boolean
		setPToFalse(N, M, P);
		// We're gonna go through each of the rules, and not consider the ending string
		// Non-Terminal
		// Building the Lexicon into the P array
		for(int i = 0; i < N; i++){
			String currentWord = st.nextToken().toLowerCase();
			// We're gonna firstly check it as a lexicon operator
			// Check if the words are equal
			for(int x = 0; x < M; x++){
				String[] currentRule = (String[]) grammar.get(x);
				// If it is a lexicon then we're gonna put that into a Tree
				if(currentRule.length == 3){
					// If the word is present within currentRule
					if(currentRule[1].equals(currentWord.toLowerCase())){
						//System.out.println(currentWord);
						P[getNonTermNumber(currentRule[0])][i][i] = new Tree(getNonTerm(currentRule[0]), i, i, currentWord, null, null, Double.parseDouble(currentRule[2]));
					}
				}
			}
		}
		for(int length = 2; length <= (N); length++){
			for(int i = 0; i < (N + 1 - length); i++){
				int j = (i + length - 1);
				for(NonTerm Current: NonTerm.values()){
					String currentToString = Current.toString();
					P[getNonTermNumber(currentToString)][i][j] = new Tree(getNonTerm(currentToString), i, j, null, null, null, 0.0);
					for (int k = i; k <= (j - 1); k++) {
						for(int f = 0; f < M; f++){
							String[] currentRule = (String[]) grammar.get(f);
							if(currentRule.length == 4){
								int currentNontermNumberOne = getNonTermNumber(currentRule[1]);
								int currentNontermNumberTwo = getNonTermNumber(currentRule[2]);
								double currentProbability = Double.parseDouble(currentRule[3]);
								if((P[currentNontermNumberOne][i][k] != null) && (P[currentNontermNumberTwo][k + 1][j] != null)){
									double newProb = P[currentNontermNumberOne][i][k].prob * P[currentNontermNumberTwo][k + 1][j].prob * currentProbability;
									if(newProb > P[getNonTermNumber(currentToString)][i][j].prob){
										P[getNonTermNumber(currentToString)][i][j].left = P[currentNontermNumberOne][i][k];
										P[getNonTermNumber(currentToString)][i][j].right = P[currentNontermNumberTwo][k + 1][j];
										P[getNonTermNumber(currentToString)][i][j].prob = newProb;
									}
								}
							}
						}
					}
				}
			}
		}
		return P;
	}
	static double totalProbability = 1;
	static boolean noWords = true;
	static StringBuilder build = new StringBuilder();
	public static void printTree1(Tree tree, int INDENT){
		if(tree.word != null){
			build.append(tree.phrase + " ");
			build.append(tree.word);
			build.append("\n");
			//totalProbability *= tree.prob;
			noWords = false;
		} else {
			build.append(tree.phrase + " ");
		}
		if(tree.left != null){
			printTree1(tree.left, INDENT);
		}
		if(tree.right != null){
			printTree1(tree.right, INDENT);
		}
	}
	
	public static void printTree(Tree[][][] P, int N){
		printTree1(P[getNonTermNumber("S")][0][N-1], 0);
		//System.out.println(P[getNonTermNumber("S")][0][N-1].prob);
		if(noWords){
			System.out.println("This sentence cannot be parsed");
		} else {
			System.out.print(build.toString());
			DecimalFormat df = new DecimalFormat("0.000000");
			//String output = df.format(totalProbability);
			String output = df.format(P[getNonTermNumber("S")][0][N-1].prob);
			System.out.println("Probability: " + output);
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader((System.in)));
		String sentence = stdin.readLine().trim().toLowerCase();
		StringTokenizer st;
		ArrayList<String[]> grammar = new ArrayList<String[]>();
		String[] rule1 = {"S", "Noun", "Verb", "0.2"};
		grammar.add(rule1);
		String[] rule2 = {"S", "Noun", "VerbAndObject", "0.3"};
		grammar.add(rule2);
		String[] rule3 = {"S", "Noun", "VPWithPPList", "0.1"};
		grammar.add(rule3);
		String[] rule4 = {"S", "NP", "Verb", "0.2"};
		grammar.add(rule4);
		String[] rule5 = {"S", "NP", "VerbAndObject", "0.1"};
		grammar.add(rule5);
		String[] rule6 = {"S", "NP", "VPWithPPList", "0.1"};
		grammar.add(rule6);
		String[] rule7 = {"NP", "Noun", "PP", "0.8"};
		grammar.add(rule7);
		String[] rule8 = {"NP", "Noun", "PPList", "0.2"};
		grammar.add(rule8);
		String[] rule9 = {"NP", "Prep", "Noun", "0.6"};
		grammar.add(rule9);
		String[] rule10 = {"NP", "Prep", "NP", "0.4"};
		grammar.add(rule10);
		String[] rule11 = {"PPList", "PP", "PP", "0.6"};
		grammar.add(rule11);
		String[] rule12 = {"PPList", "PP", "PPList", "0.4"};
		grammar.add(rule12);
		String[] rule13 = {"VerbAndObject", "Verb", "Noun", "0.5"};
		grammar.add(rule13);
		String[] rule14 = {"VerbAndObject", "Verb", "NP", "0.5"};
		grammar.add(rule14);
		String[] rule15 = {"VPWithPPList", "Verb", "PP", "0.3"};
		grammar.add(rule15);
		String[] rule16 = {"VPWithPPList", "Verb", "PPList", "0.1"};
		grammar.add(rule16);
		String[] rule17 = {"VPWithPPList", "VerbAndObject", "PP", "0.4"};
		grammar.add(rule17);
		String[] rule18 = {"VPWithPPList", "VerbAndObject", "PPList", "0.2"};
		grammar.add(rule18);
		String[] lexicon1 = {"Noun", "amy", "0.1"};
		grammar.add(lexicon1);
		String[] lexicon2 = {"Noun", "dinner", "0.2"};
		grammar.add(lexicon2);
		String[] lexicon3 = {"Noun", "fish", "0.2"};
		grammar.add(lexicon3);
		String[] lexicon4 = {"Noun", "streams", "0.1"};
		grammar.add(lexicon4);
		String[] lexicon5 = {"Noun", "swims", "0.2"};
		grammar.add(lexicon5);
		String[] lexicon6 = {"Noun", "tuesday", "0.2"};
		grammar.add(lexicon6);
		String[] lexicon7 = {"Prep", "for", "0.5"};
		grammar.add(lexicon7);
		String[] lexicon8 = {"Prep", "in", "0.3"};
		grammar.add(lexicon8);
		String[] lexicon9 = {"Prep", "on", "0.2"};
		grammar.add(lexicon9);
		String[] lexicon10 = {"Verb", "ate", "0.7"};
		grammar.add(lexicon10);
		String[] lexicon11 = {"Verb", "streams", "0.1"};
		grammar.add(lexicon11);
		String[] lexicon12 = {"Verb", "swim", "0.2"};
		grammar.add(lexicon12);
		st = new StringTokenizer(sentence); // for the next loop
		int size = st.countTokens();
		for(int i = 0; i < size; i++){
			String currentWord = st.nextToken().toLowerCase();
			boolean wordIsThere = true;
			for(int x = 0; x < grammar.size(); x++){
				String[] currentRule = (String[]) grammar.get(x);
				if(currentRule.length == 3){
					if(!(currentRule[1].equals(currentWord.toLowerCase()))){
						wordIsThere = false;
					} else {
						wordIsThere = true;
						break;
					}
				}
			}
			if(wordIsThere == false){
				System.out.println("This sentence cannot be parsed");
				System.exit(1);
			}
		}
		Tree[][][] P = CYKParse(sentence, grammar);
		printTree(P, size);
	}
}
