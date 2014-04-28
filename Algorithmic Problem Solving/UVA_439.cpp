/*
- Knight Moves
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
#include <sstream>
#include <queue>
using namespace std;

int main(void){
    int positionOne = 0, positionTwo = 0;
    char digitOne, digitTwo;
    while(cin >> positionOne  >> digitOne >> positionTwo >> digitTwo){

        queue<int> q;
        vector<int> dist = vector<int>();
        dist[0] = 0;
        while (!q.empty()) {
    int u = q.front(); q.pop();                        // queue: layer by layer!
    if (dist[u] != layer) printf("\nLayer %d: ", dist[u]);
    layer = dist[u];
    printf("visit %d, ", u);
    for (int j = 0; j < (int)AdjList[u].size(); j++) {
      ii v = AdjList[u][j];                           // for each neighbors of u
      if (dist[v.first] == 1000000000) {
        dist[v.first] = dist[u] + 1;                  // v unvisited + reachable
        p[v.first] = u;          // addition: the parent of vertex v->first is u
        q.push(v.first);                              // enqueue v for next step
      }
      else if ((dist[v.first] % 2) == (dist[u] % 2))              // same parity
        isBipartite = false;
  } }
    }
}
