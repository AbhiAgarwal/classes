/*
- Lift Hopping
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#define MAX_N 5
#define MAX_T 150
#define INF 1000000000 // defined in book

#include <algorithm>
#include <iostream>
#include <vector>
#include <cstdio>
#include <cstring>
#include <sstream>
#include <queue>
using namespace std;

vector<int> times;
vector<int> elevatorMovement[MAX_N];
vector<int> lineNumbers;

// n (1<=n<=5) elevators which travel up and down at (possibly) different speeds
// You are on floor 0 and would like to get to floor k as quickly as possible.
int n = 0, k = 0;
// OUTPUT time
int currentTimeOutput = 0;

/*
- Run Dijkstra on the graph of (elevator,floor) pairs.
- The queue is initialized with all pairs of the form (e,0) where elevator e can go to floor 0.
- Remember that switching elevators requires 60 seconds.
*/
// Dijkstra implementation from:: book of APS
void dijkstra(){
    int s = 0;
    // reset whole arrays
    vector<int> dist(MAX_T, INF);
    vector<bool> visited(MAX_T, false);
    // getting into implementation
    dist[s] = 0;
    // Start
    priority_queue< pair<int, int>, vector<pair<int, int> >, greater<pair<int, int> > > pq;
    pq.push(make_pair(dist[s], s));
    // The queue is initialized with all pairs of the form (e,0) where elevator e can go to floor 0
    // start dijkstra
    while (!pq.empty()){
        pair<int, int> front = pq.top();
        pq.pop();
        int d = front.first;
        int u = front.second;
        if (d > dist[u]){
            continue;
        }
        for (int j = 0; j < (int)elevatorMovement[u].size(); j++) {
          pair<int, int> v = elevatorMovement[u][j];
          int currentTimeValue = 0;
          if (dist[u] + v.second < dist[v.first]){
            dist[v.first] = dist[u] + v.second;
            pq.push(ii(dist[v.first], v.first));

  }
}
}
    // OUTPUT: currentTimeOutput = -1 OR currentTimeOutput = someIntegerNumber
}

int main(){
    while(scanf("%d %d", &n, &k) != EOF){
        currentTimeOutput = 0, n = 0, k = 0;
        times.clear();
        times.assign(n, 0);
        lineNumbers.clear();
        lineNumbers.assign(n, 0);
        for(int i = 0; i < MAX_N; i++){
            elevatorMovement[i].clear();
            elevatorMovement[i].assign(n, 0);
        }
        // The next line will contain the numbers T1, T2,... Tn
        for(int i = 0; i < n; ++i){
            int enteredNumber = 0;
            scanf("%d", &enteredNumber);
            times[i] = enteredNumber;
        }
        // Finally, the next n lines will contain sorted lists of integers - the first line will list the floors visited by elevator number 1, the next one will list the floors visited by elevator number 2, etc.
        string currentLine;
        // Not sure but there was an empty line...? ehh thanks hints.
        getline(cin, currentLine);
        // O(n * lineNumber?)
        for(int i = 0; i < n; ++i){
            // ** Best is StringStream as it allows us to go through each of the values using cin >>
            int lineSize = 0;
            // Parse through and get the line until '\n'
            getline(cin, currentLine);
            // Turns it into a readable line
            stringstream lineInformation(currentLine);
            // int currentFloor = 0;
            int enteredNumber = 0;
            // Going through all the stringstream states and getting it
            while(lineInformation >> enteredNumber){
                elevatorMovement[i].push_back(enteredNumber);
                lineSize++;
            }
            lineNumbers[i] = lineSize;
        }
        dijkstra();
        // If it is impossible to do, print "IMPOSSIBLE" instead
        if(currentTimeOutput < 0){
            printf("%s\n", "IMPOSSIBLE");
        } else {
            printf("%d\n", currentTimeOutput);
        }
    }
}
