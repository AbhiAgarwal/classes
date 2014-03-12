/*
- Place the Guards
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Standard BFS from the book and the adapted to the problem
NOT working for 11080_7, 11080_2
*/

#include <stdio.h>
#include <algorithm>
#include <cstring>
#include <vector>
#include <iostream>
#include <string.h>
#include <cstdlib>
#include <string>
#include <queue>
using namespace std;

// pair<x, y> is stupid here.. we don't need a 0.-.-.
vector<vector<int> > AdjList;
vector<int> colors;

// find the minimum number of guards needed to guard all the junctions and streets of his country
// Standard BFS from the book & notes
int searchBFS(int s, int numberOfJunctions, int numberOfStreets){
    // Setting current loop value to 0
    colors[s] = 0;
    // Set queue and push current loop value into it
    queue<int> q;
    q.push(s);
    // p is basically the colors array, 2 colors possible
    int p[2];
    // Increment the first color, as we already allocated it
    p[0] = 1; p[1] = 0;
    // int layer = -1;
    // int toReturn = -1;
    while (!q.empty()){
        int u = q.front();
        q.pop();
        for(int j = 0; j < (int)AdjList[u].size(); ++j){
            int v = AdjList[u][j];
            // If it hasn't been colored yet
            if (colors[v] == 1<<30){
                colors[v] = 1 - colors[u];
                p[colors[v]]++;
                q.push(v);
            } else if ((colors[v]) == (colors[u])){
                // If coloring problem can't occur
                return -1;
            }
        }
    }
    // We want the minimialized color to be used
    // First minimized color
    if(p[0] > 0 && p[1] > 0){ return min(p[0], p[1]); }
    // Special Case: 0, 1 then return 1
    // when we are doing (below) it will cause a problem
    // 1
    // 1 0
    else { return max(p[0], p[1]); }
}

int main(){
    int numberOfTestCases = 0;
    // number of test cases
    scanf("%d", &numberOfTestCases);
    while(numberOfTestCases--){
        // v is the number of junctions and e is the number of streets
        int numberOfJunctions /*v*/ = 0, numberOfStreets /*e*/ = 0;
        scanf("%d %d", &numberOfJunctions, &numberOfStreets);
        AdjList.assign(numberOfJunctions + 1, vector<int>());
        // Each of the next e line contains 2 integer f and t denoting that there is a street between f and t.
        for(int i = 0; i < numberOfStreets; ++i){
            int x = 0, y = 0;
            cin >> x >> y;
            // ehh pair<x, y> not needed -.-
            AdjList[x].push_back(y);
            AdjList[y].push_back(x);
        }
        // We have to create a driver to drive the algorithm
        // As there are more than just one potential case
        colors.assign(numberOfJunctions, 1<<30);
        int finalAnswer = 0;
        for(int i = 0; i < numberOfJunctions; ++i){
            if(colors[i] == 1<<30){
                int answer = searchBFS(i, numberOfJunctions, numberOfStreets);
                if(answer == -1){
                    finalAnswer = -1; break;
                } else {
                    finalAnswer += answer;
                }
            }
        }
        // Set the value of m as -1 if it is impossible to place the guards without fighting
        printf("%d\n", finalAnswer);
    }
}
