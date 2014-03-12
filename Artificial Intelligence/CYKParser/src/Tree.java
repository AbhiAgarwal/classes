public class Tree {  
	NonTerm phrase;
    int startPhrase;
    int endPhrase;
    String word;
    Tree left;
    Tree right;
    double prob;
    public Tree(NonTerm nonTerm, int i, int i2, String currentWord,
			Tree leftTree, Tree rightTree, double d){
		this.phrase = nonTerm;
		this.startPhrase = i;
		this.endPhrase = i2;
		this.word = currentWord;
		this.left = leftTree;
		this.right = rightTree;
		this.prob = d;
	}
}