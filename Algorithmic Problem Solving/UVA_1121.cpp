/*
- Subsequence
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
#include <limits>
using namespace std;

typedef vector<int> vecInt;
#define INF std::numeric_limits<int>::max()

int main(void){
    int N = 0, S = 0;
    while(scanf("%d %d", &N, &S) == 2){
        int currentMinimum = INF, currentPoint = 0, ij = 0, currentValue = 0;
        /* Parse */ vecInt p = vecInt();
        // The numbers of the sequence are given in the second line of the test case, separated by intervals
        for(int i = 0; i < N; ++i){scanf("%d", &currentPoint);
            p.push_back(currentPoint);
          // Sum so we can get Subsequence
        } for(int i = 1; i <= N; ++i){ p[i]+=p[i-1]; }
        // Search through the States, moving between each index
        // Go through each index
        for(int ii = 0; ii < N; ++ii){
            currentValue = 0;
            // Only increment the second index when certain conditions are true
            // index of j <= index of i
            // the sum between them is less than S
            // then the difference will be index i - index j
            while(ij<=ii && (p[ii]-p[ij]>=S) && (currentValue=ii-ij)){
                currentMinimum = min(currentMinimum, currentValue); ij++;
            }
        }

        // Output
        // If there isn't such a subsequence, print 0 on a line by itself.
        if(currentMinimum == INF){ currentMinimum = 0; }
        // For each the case the program has to print the result on separate line of the output file
        cout << currentMinimum << endl;
    }
}
