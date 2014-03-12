/*
- Ordering Tasks
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Used notes from book
- ch4_01_dfs from the book
*/

// Taken it from the book notes
#define PROCESSING 0
#define PREFINISHED 1

#include <stdio.h>
#include <algorithm>
#include <cstring>
#include <vector>
using namespace std;

// Adjusted from the book as we don't need pairs anymore
// This is only a single dimensional point
vector<vector<int> > AdjList;
vector<int> topoSort;
vector<int> dfs_num;

// Taken from the book notes::ch4_01_dfs
// In computer science, a topological sort of a directed graph is a linear ordering of its vertices such that for every directed edge uv from vertex u to vertex v, u comes before v in the ordering.
void topologicalSort(int u){
    dfs_num[u] = PREFINISHED;
    for(int j = 0; j < (int)AdjList[u].size(); j++){
        int v = AdjList[u][j]; // Adjusted as we don't need pairs
        if (dfs_num[v] == PROCESSING){
            topologicalSort(v);
        }
    }
    topoSort.push_back(u);
}

int main(){
    int numberOfTasks = 0, directPrecedence = 0;
    int iNum = 0, jNum = 0, x = 0, m = 0, y = 0, a = 0;
    while(scanf("%d %d", &numberOfTasks, &directPrecedence) == 2){
        // If the next case is 0 0
        if(numberOfTasks == 0 && directPrecedence == 0){
            break;
        }
        topoSort.clear();
        //printf("%d %d\n", numberOfTasks, directPrecedence);
        // Had no idea this worked until using the book
        AdjList.assign(numberOfTasks+1, vector<int>());
        for(x = 1; x <= directPrecedence; x++){
            scanf("%d %d", &iNum, &jNum);
            // Conversion to ZERO index, Was getting an error here
            // You have to initialize AdjList.. duh..
            // Oh just converted everything else properly
            AdjList[iNum].push_back(jNum);
        }
        dfs_num.assign(numberOfTasks+1, PROCESSING);
        for(m = 1; m <= numberOfTasks; m++){
            // If the portion hasn't been processed than pass it through
            // the topological sort
            if (dfs_num[m] == PROCESSING){
                topologicalSort(m);
            }
        }
        // Reverse
        for(y = ((int)topoSort.size() - 1); y >= 1; y--){
            // To readjust the 0th Index
            printf("%d ", (topoSort[y]));
        }
        printf("%d\n", (topoSort[0]));
    }
    return 0;
}
