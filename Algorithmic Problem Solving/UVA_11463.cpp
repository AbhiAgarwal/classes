/*
- Commandos
- Time Limit:2000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- After running all pairs shortest path, the answer can be computed quickly
- Floyd Warshall?
- 0-indexed
- MAX int (2^31 - 1):
    -> int a = std::numeric_limits<int>::max();
*/

#include <cstdio>
#include <algorithm>
#include <vector>
#include <string.h>
#include <limits>
using namespace std;

#define infinity numeric_limits<int>::infinity() + 1

int main(){
    int T = 0, currentCase = 1;
    scanf("%d", &T);
    while(T--){
        // N denotes the number of buildings in the head quarter
        // R is the number of roads connecting two buildings
        // R lines contain two distinct numbers
        int N = 0, R = 0;
        scanf("%d", &N);
        scanf("%d", &R);
        // Index connected to Assignment
        // Diagonals to 0, and non to 1
        int buildingConnect[N][N];
        // all elements not considered are set to infinity first
        memset(buildingConnect, infinity, sizeof(buildingConnect[0][0]) * N * N);
        // Set Diagnoals to 0
        for(int i = 0; i < N; i++){
            // u->u == u->u == 0
            buildingConnect[i][i] = 0;
        }
        // two distinct numbers,  0 ≤ u, v < N
        // this means there is a road connecting building u to building v.
        int u = 0, v = 0;
        for(int i = 0; i < R; i++){
            scanf("%d %d", &u, &v);
            // u->v == v->u == 1 here
            buildingConnect[u][v] = 1;
            buildingConnect[v][u] = 1;
        }
        // Run Floyd Warshall - From book, Running all pairs shortest path
        for (int k = 0; k < N; k++){
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    buildingConnect[i][j] = min(buildingConnect[i][j], buildingConnect[i][k] + buildingConnect[k][j]);
                }
            }
        }
        // The last line of each case contains two integers  0 ≤ s, d < N
        // s denotes the building from where the mission starts
        // d denotes the building where they must meet
        int s = 0, d = 0;
        scanf("%d %d", &s, &d);
        // You may assume that two buildings will be directly connected by at most one road. The input will be such that, it will be possible to go from any building to another by using one or more roads.
        int finalAnswer = 0;
        for(int i = 0; i < N; i++){
            // maximal size of building s to i and i to d
            // we've already done all pairs shortest path so we can do this quickly!
            finalAnswer = max(finalAnswer, buildingConnect[s][i] + buildingConnect[i][d]);
        }
        // Print Answer
        printf("Case %d: %d\n", currentCase, finalAnswer);
        currentCase++;
    }
}
