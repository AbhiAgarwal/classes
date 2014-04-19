#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <queue>
#include <cstdlib>
#include <limits>
#include <map>
#include <algorithm>
#include <sstream>
using namespace std;

typedef vector<string> tpair;
typedef vector<string> npair;
typedef map<string, int> dmap;
typedef map<string, int>::iterator dmapiterator;

npair allNodes; // A, B, C, D, E, F, G, etc.
npair allTreasures;
int numberOfNodes = 0; // count of allNodes
int stepNumber = 0; // number of maximum steps

class Node {
  public:
    string id;
    tpair treasures, tolls;
    bool visited;
    tpair next;
    void printInformation(void);
};

void Node::printInformation(void){
    printf("ID: %s\n", this->id.c_str());
    printf("Treasures: ");
    for(int i = 0; i < this->treasures.size(); ++i){
        printf("%s ", this->treasures[i].c_str());
    }
    printf("\n");
    printf("Tolls: ");
    for(int i = 0; i < this->tolls.size(); ++i){
        printf("%s ", this->tolls[i].c_str());
    }
    printf("\n");
    printf("Visited: %d\n", this->visited);
    printf("Next: ");
    for(int i = 0; i < this->next.size(); ++i){
        printf("%s ", this->next[i].c_str());
    }
    printf("\n\n");
};

typedef vector<Node> singleNode;
typedef singleNode::iterator singleNodeIterator;
typedef vector<string> singleSequence;

singleNode all;

int main(void){
    /* Variable Decleration */
    string nextSentence = "";
    /* GET START LINE */
    getline(cin, nextSentence);
    istringstream currentStart(nextSentence);
    while(currentStart){
        if(!getline(currentStart, nextSentence, ' ')){ break; }
        if(strcmp(nextSentence.c_str(), "")){
            allNodes.push_back(nextSentence.c_str());
        }
    }
    /* Update Number of Nodes */
    numberOfNodes = allNodes.size();
    /* TREASURES */
    getline(cin, nextSentence);
    istringstream currentTreasure(nextSentence);
    while(currentTreasure){
        if(!getline(currentTreasure, nextSentence, ' ')){ break; }
        if(strcmp(nextSentence.c_str(), "")){
            allTreasures.push_back(nextSentence.c_str());
        }
    }
    /* NUMBER OF STEPS */
    getline(cin, nextSentence);
    stepNumber = atoi(nextSentence.c_str());

    /* START TREASURES */
    /* GOING THROUGH EACH NODE INFORMATION */
    for(int i = 0; i < numberOfNodes; ++i){
        getline(cin, nextSentence);
        istringstream currentStartArea(nextSentence);
        int currentPosition = 0;
        bool treasureDone = false, tollDone = false;
        /* New Node Decleration */
        Node newNode;
        while(currentStartArea){
            // Initializes a new Node(...);
            if(!getline(currentStartArea, nextSentence, ' ')){ break; }
            // FIRST LETTER - POSITION
            if(currentPosition == 0){ newNode.id = nextSentence.c_str();}
            else {
                // If NEXT is present then TRUE
                // NEXT
                if(!strcmp(nextSentence.c_str(), "TOLLS")){treasureDone = true;}
                if(!strcmp(nextSentence.c_str(), "NEXT")){tollDone = true;}
                // After TREASURE we take the information
                // TREASURES
                if(!treasureDone && strcmp(nextSentence.c_str(), "TREASURES")){
                    newNode.treasures.push_back(nextSentence.c_str());
                }
                if(!tollDone && treasureDone && strcmp(nextSentence.c_str(), "TOLLS")){
                    newNode.tolls.push_back(nextSentence.c_str());
                }
                // After NEXT we get the NEXT Information
                // NEXT
                if(treasureDone && tollDone && strcmp(nextSentence.c_str(), "NEXT")){
                    newNode.next.push_back(nextSentence.c_str());
                }
            }
            currentPosition++;
        }
        all.push_back(newNode);
    }

    return 0;
}
