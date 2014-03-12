/*
- Ingenuous Cubrency
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
 */

#include <cstdio>
#include <cstring>
#include <math.h>
using namespace std;

// You may assume that all the amounts are positive
// and less than 10000
long long waysToPay[50000];
int cubrency[50]; // just for safekeeping

int main(){
    int maxCategory = 21;
    // Loop & N
    int i, x, j, N = 0;
    // Initialize all to 0
    memset(cubrency, 0, sizeof(cubrency));
    // coins with the denominations of 1, 8, 27, ..., up to 9261 cubes, are available in Cubeland
    for(int i = 1; i <= maxCategory; i++){
        cubrency[i] = pow(i, 3);
    }
    // Going through each case
    while(scanf("%d", &N) != EOF){
        memset(waysToPay, 0, sizeof(waysToPay));
        waysToPay[0] = 1; // so we don't screw up multiplication
        for(x = 1; x <= maxCategory; x++){
            // given amounts to be paid output one line containing a single integer representing the number of ways to pay the given amount using the coins available in Cubeland
            for(j = 1; j <= 10000; j++){
                // if the incrementing J is greater than x^3
                if(j >= cubrency[x]){
                    //
                    waysToPay[j] += waysToPay[j - cubrency[x]];
                }
            }
        }
        printf("%lld\n", waysToPay[N]);
    }
    return 0;
}
