/*
- Glass Beads
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- AHA moment in class to realize what a and b were
- OOPS WRONG SIZE (HAD TO BE SIZE OF 2 NOT 1)
*/

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
#include <sstream>
#include <map>
#include <string>
#include <ostream>
#include <limits>
using namespace std;

struct SuffixTree {
    static const int INF = 1<<30;
    int last;
    string str;
    struct Node;

    struct Edge {
        int a,b;
        Node *end;
        SuffixTree* tree;
        Edge(int a, int b, Node *end, SuffixTree* tree) : a(a), b(b), end(end), tree(tree) {}
        char getFirst() const{
            return tree->str[a];
        }
        int size() const{
            return min(tree->last,b)-a+1;
        }
        friend ostream& operator<<(ostream& out, Edge& e);
    };

    struct Node {
        map<char,Edge*> edges;
        Node* suffix;
        int a,b;
        SuffixTree* tree;
        Node(SuffixTree* tree) : tree(tree) {}
        void add(Edge* e){
            edges[e->getFirst()] = e;
        }
        Edge* operator[](char c){
            return edges[c];
        }
        int numChildren(){
            return edges.size();
        }
        friend ostream& operator<<(ostream& out, Node&);
    };

    Node *root, *sentinel;
    Node* getRoot(){
        return root;
    }
    const string& getStr() const{
        return str;
    }
    void setPrefix(Node* n, Edge* e, int len){
        map<char,Edge*>::iterator it = n->edges.begin();
        for(; it != n->edges.end(); ++it){
            Edge *edge = it->second;
            setPrefix(edge->end,edge,len+min(edge->b,last)-edge->a+1);
        }
        if(e){
            n->b = min(e->b,last); n->a = n->b - len + 1;
        } else {
            n->a = 0; n->b = -1;
        }
    }

    Node* testAndSplit(Node* s, int k, int p, char c){
        if (k > p) return s == sentinel ? NULL : (*s)[c] == NULL ? s : NULL;
        Edge* e = (*s)[str[k]];
        if (c == str[e->a+p-k+1]) return NULL;
        Node* r = new Node(this);
        Edge* re = new Edge(e->a+p-k+1,e->b,e->end,this);
        r->add(re);
        Edge* se = new Edge(e->a,e->a+p-k,r,this);
        s->add(se);
        return r;
    }

    Node* canonize(Node* s, int& k, int p){
        if (p < k) return s;
        if (s == sentinel) { s = root; ++k; if (p < k) return s; }
        Edge* e = (*s)[str[k]];
        while (e->b - e->a <= p - k)
        {
            k = k + e->b - e->a + 1;
            s = e->end;
            if (k <= p) e = (*s)[str[k]];
        }
        return s;
    }

    Node* update(Node* s, int& k, int i){
        Node *oldr = root, *r = testAndSplit(s,k,i-1,str[i]);
        while(r){
            Node* rp = new Node(this);
            Edge* e = new Edge(i,INF,rp,this);
            r->add(e);
            if (oldr != root) oldr->suffix = r;
            oldr = r;
            s = canonize(s->suffix,k,i-1);
            r = testAndSplit(s,k,i-1,str[i]);
        }
        if(oldr != root) oldr->suffix = s;
        return s;
    }

    void buildTree(){
        root = new Node(this);
        sentinel  = new Node(this);
        root->suffix = sentinel;
        Node* s = root;
        int k = 0;
        last = -1;
        for(int i = 0; i < (int)str.size(); ++i){
            ++last;
            s = update(s,k,i);
            s = canonize(s,k,i);
        }
    }

    int fix(int x) const{
        return x == INF ? last : x;
    }
    friend ostream& operator<<(ostream& out, SuffixTree& t);
    ostream& prettyFormat(ostream& out, Node* n, int tab) const{
        out << string(tab,' ') << "\"" << *n << "\"" << endl;
        map<char,Edge*>::iterator it = n->edges.begin();
        for(; it != n->edges.end(); ++it){
            Edge* e = it->second;
            cout << string(tab,' ') << it->first << " : " << *e << " = " << e->a << ',' << fix(e->b) << ',' << e->size() << endl;
            prettyFormat(out,e->end,tab+2);
        }
        return out;
    }
    SuffixTree(const string& str) : str(str){
        buildTree();
        setPrefix(root,NULL,0);
    }
};

ostream& operator<<(ostream& out, SuffixTree::Edge& e){
    return out << e.tree->str.substr(e.a,e.size());
}
ostream& operator<<(ostream& out, SuffixTree::Node& n){
    return out << n.tree->str.substr(n.a,min(n.b,n.tree->last)-n.a+1);
}
ostream& operator<<(ostream& out, SuffixTree& t){
    return t.prettyFormat(out, t.root, 0);
}

/*
    - If the string is x, find the lexicographically first substring of xx (x concatenated with itself) of length |x|. This can be done quickly with a suffix tree
*/

// DFS through the Sufix Tree
int dfs(SuffixTree::Node *node, int currentPosition, string currentCase, int currentLength, int halflength){
    // containing all the suffixes of the given text as their keys and positions in the text as their value
    //printf("(cp %d cl %d) ", currentPosition, currentLength);
    if(node->b >= (halflength)){
        // If we reach the end then we want to return them
        return node->a;
    } else {
        // else then we have to run the DFS
        map<char, SuffixTree::Edge *> currentEdges = node->edges;
        // Iterate through the map
        for(auto currentEdge: currentEdges){
            //printf("%c ", currentEdge.first); printf("%c ", currentEdge.first); printf("%d ", currentEdge.second->end->numChildren());
            SuffixTree::Edge *secondEdge = currentEdge.second;
            //printf("%d", currentEdge.first); printf("%d ", returnAnswer); printf("%c\n", secondEdge->getFirst());
            return dfs(currentEdge.second->end, currentPosition + 1, currentCase, currentLength, halflength);
        }
    }
    // control may reach end of non-void function
    return 0;
}

/* - First do a DFS and compute the length of the maximum length substring that occurs at least k times */

// Driver to DFS
int disjoining(string currentCase){
    string stringCocatenated = (currentCase + currentCase);
    SuffixTree currentTree(stringCocatenated);
    int stringLengthCurrentCase = currentCase.length();
    //int stringLengthCocatenated = stringCocatenated.length();
    SuffixTree::Node *rootNode = currentTree.getRoot();

    // The suffix tree for the string S of length n is defined as a tree such that
    //  - The tree has exactly n leaves numbered from 1 to n
    //  - Except for the root, every internal node has at least two children
    //  - Each edge is labeled with a non-empty substring of S.
    //  - No two edges starting out of a node can have string-labels beginning with the same character
    //  - The string obtained by concatenating all the string-labels found on the path from the root to leaf i spells out suffix S[i..n], for i from 1 to n
    // return the answer that we get from the DFS

    return dfs(rootNode, 0, currentCase, stringLengthCurrentCase, currentCase.length());
}

int main(void){
    // The input consists of N cases
    int numberOfCases = 0, finalAnswer = 0;
    scanf("%d", &numberOfCases);
    while(numberOfCases--){
        // Reset
        finalAnswer = 0;
        /* INPUT
            - Each case consists of exactly one line containing necklace description, and each bead is represented by a lower-case character of the english alphabet (a-z)*/
        string currentCase;
        cin >> currentCase;
        /* PROCESS
            - number of the bead which is the first at the worst possible disjoining
            - The description of the necklace is a string $A =a_1a_2 \dots a_m$ specifying sizes of the particular beads, where the last character am is considered to precede character a1 in circular fashion*/
        // If the string is x, find the lexicographically first substring of xx (x concatenated with itself) of length |x|
        // Now we use the tree to find the answer
        finalAnswer = disjoining(currentCase);
        //printf("\n");
        /* OUTPUT */
        printf("%d\n", (finalAnswer + 1));
    }
}
