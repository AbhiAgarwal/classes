/*
- Component Placement
- Input: Standard Input
- Output: Standard Output
*/

#define MAX_N 200
#define MAX_M 100000

#include <cstring>
#include <cstdio>
#include <algorithm>
#include <iostream>
#include <vector>
using namespace std;

vector<int> costTop;
vector<int> costBottom;
vector<int> placement;
int connectionCost[MAX_M][MAX_M];

int main(){
    int T = 0;
    scanf("%d", &T);
    while(T--){
        // N = number of components
        // M = number of interconnections
        int N = 0, M = 0;
        scanf("%d %d", &N, &M);
        // Clear
        costTop.clear();
        costTop.assign(N + 1, 0);
        costBottom.clear();
        costBottom.assign(N + 1, 0);
        placement.clear();
        placement.assign(N + 1, 0);
        memset(connectionCost, -1, sizeof(connectionCost[0][0]) * (M + 1) * (M + 1));
        // This will be followed by N integers in a line, each between 1 and 10000000 (inclusive), where i-th of it describes the cost of placing the component on the top layer
        int tempNumber = 0;
        for(int i = 1; i <= N; i++){
            scanf("%d", &tempNumber);
            costTop[i] = tempNumber;
        }
        // The next line contains N more integers, each between 1 and 10000000 (inclusive), where i-th of it denotes the cost of placing it on the bottom layer
        tempNumber = 0;
        for(int i = 1; i <= N; i++){
            scanf("%d", &tempNumber);
            costBottom[i] = tempNumber;
        }
        // The next line contains N more integers, each will be either 0, -1 or +1, where -1 means i-th component can only be placed on the bottom, +1 means i-th component can only be placed on the top, 0 means the component can be placed on either side
        tempNumber = 0;
        for(int i = 1; i <= N; i++){
            scanf("%d", &tempNumber);
            placement[i] = tempNumber;
        }
        // Then there will be M lines, each containing three integers, p, q, and r (1 <= p, q <= N, 1 <= r <= 10000000), denoting that, p and q-th component has to be interconnected and if they are on different layers, the cost of interconnection will be r. There will be at most one interconnection between any pair or components.
        int p = 0, q = 0, r = 0;
        for(int i = 1; i <= M; i++){
            scanf("%d %d %d", &p, &q, &r);
            // Travel from and to p<->q
            connectionCost[p][q] = r;
            connectionCost[q][p] = r;
        }
        // For each test case, output the minimum cost to place the components
    }
}
