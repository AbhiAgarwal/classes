/*
- Inspector's Dilemma
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Error::: forgot to increment to memset(V+1) when I updated to using the 0-indexed based values.................. ugh
*/

#define MAX_V 1500
// Purpose: count odds.
#include <cstring>
#include <cstdio>
#include <algorithm>
#include <iostream>
#include <vector>
using namespace std;

vector<int> edges;
vector<int> cities[MAX_V];
bool visited[MAX_V];

int edgeCount = 0;
int globalNumberOfOddVerticies = 0;
int currentVarb = 0;

void dfs(int i){
    currentVarb++;
    if(visited[i]){
        return;
    } else {
        visited[i] = true;
        edgeCount++;
        // THE value is ODD!!
        if(edges[i] % 2 == 1){
            // If it is odd then increment!
            globalNumberOfOddVerticies++;
        }
        // Iterate through all possibilities of values
        for(int m = 0; m < cities[i].size(); m++){
            if(!visited[cities[i][m]]){
            // Go through the VALUE in each city NOT m...??? duh
                dfs(cities[i][m]);
            }
        }
    }
}

int main(){
    // V (1 ≤ V ≤ 1000), the number of cities
    // E (0 ≤ E ≤ V * (V-1) / 2), the number of highways the inspector needs to check
    // T (1 ≤ T ≤ 10), time needed to pass a single highway
    int V = 0, E = 0, T = 0;
    int caseNumber = 1;
    while(scanf("%d %d %d", &V, &E, &T)){
        if((V == 0 && E == 0 && T == 0)){
            break;
        }
        // Initialize each to the highest
        for(int i = 0; i < V; i++){
            cities[i].clear();
            cities[i].assign(V + 1, 0);
        }
        edges.clear();
        edges.assign(V + 1, 0);
        // we only have to visit each city once
        memset(visited, false, V + 1);
        // two integers a and b (1 ≤ a,b ≤ V, a != b) meaning the inspector has to check the highway between cities a and b
        int a = 0, b = 0;
        // E lines contains two integers a and b
        // Go through each highway we have to parse
        // INDEX 1 NOW.....
        for(int i = 1; i <= E; i++){
            // Parse
            scanf("%d %d", &a, &b);
            // Link them to one other because they both connect to eachother
            // they are a highway that goes 2 ways
            // A/B is 1 ≤ a,b ≤ V so to get 0-index A-1&B-1
            // There can be more than 1 city connected to the other
            cities[a].push_back(b);
            cities[b].push_back(a);
            // As we need to see how many verticies have more vertex going out/in.
            edges[a]++;
            edges[b]++;
        }
        // For each test case, print the serial of output followed by the minimum possible time the inspector needs to inspect all the highways on his list. Look at the output for sample input for details
        // int finalAnswer = 0;
        // E (0 ≤ E ≤ V * (V-1) / 2), the number of highways the inspector needs to check - -1 to 0index
        int finalAnswerHighways = E - 1;
        bool isFirst = false;
        // Run the main algorithm
        // Go through each city and time it yo
        for(int x = 1; x <= V; x++){
            if(!visited[x]){ // not visited, and NOT edgeless?
                // count the number of odd degree nodes
                    if(edges[x] == 0){
                        // If edges are 0
                        visited[x] = true;
                    } else {
                        // If edges are NOT 0
                        edgeCount = 0;
                        globalNumberOfOddVerticies = 0;
                        currentVarb = 0;
                            //if(!isFirst){
                            //    isFirst = true;
                            //} else {
                            //    finalAnswerHighways++;
                            //}
                            // Increase number of highways as we go along (we can take care of this later)
                        finalAnswerHighways++;
                        dfs(x);
                        if(globalNumberOfOddVerticies > 0){
                            // Odd verticies count goes up
                            // There exist odd verticies so the person will have to travel half as many more highways to cover it now.
                            // lets say we have 6 global odd verticies, then we need 2 new highways to fix all that up and allow him to travel all the way through.
                            // It will always work because odd verticies must be even:: no truncation error
                            finalAnswerHighways += (globalNumberOfOddVerticies/2) - 1;
                        }
                    }
                }
        }
        if(finalAnswerHighways < 0){
            finalAnswerHighways = 0;
        }
        // Each Path as weight 1 so we can just do T * finalAnswerHighways to get the time.. 0 index.
        printf("Case %d: %d\n", caseNumber, finalAnswerHighways * T);
        caseNumber++;
    }
}
