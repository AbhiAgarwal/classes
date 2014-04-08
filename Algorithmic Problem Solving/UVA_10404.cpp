/*
- Bachet's Game
- Time Limit:6666MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#define MAX_CACHE 2000000
#define MAX_S 20

#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
using namespace std;

int main(){
    // n = the number of stones on the table
    // m = giving the number of numbers that follow
    int n = 0, m = 0;
    int cache[MAX_CACHE]; // n*m = 1000000*10
    int stonesRemoved[MAX_S]; // m <= 10
    while(scanf("%d %d", &n, &m) != EOF){
        // Parse + Initialization
        int nextNumber = 0;
        memset(stonesRemoved, 0, sizeof(int) * MAX_S);
        memset(cache, 0, sizeof(int) * MAX_CACHE);
        for(int i = 0; i < m; ++i){
            scanf("%d", &nextNumber);
            stonesRemoved[i] = nextNumber;
        }
        // MAX per: 1000000 * 10 = 10 million per N/M combo MAX
        // Now we attempt to seek all the values
        // We have a huge time so we can do it pretty much
        // iteratively
        // ** The winner is the one to take the last stone

        // i is number of stones on each table
        // so we're iterating through all the possible number of stones
        for(int i = 0; i <= n; ++i){
            // m is how many stones can be removed from the table
            // single move must be a member of a certain set of m numbers
            // Don't repeat elements again
            if(cache[i] == 0){
                for(int x = 0; x < m; x++){
                    // not more than k stones from the table
                    // So we basically have to see how many stones we can remove off the table on every stone that we can have until n
                    // If we execute on this properly we only need to access the last stone
                    // Foreach N we are just trying to remove stones and see who wins when we remove all the stones and we have i stones on the table, and if we reach n, which is the number of stones on the table then we've crossed the limit
                    // Every stone amount possible + the number of stones to remove = who wins at the end
                    int currentStoneNumber = i + stonesRemoved[x];
                    // We start with i == 0 so have to move up
                    if(currentStoneNumber <= n){
                        cache[currentStoneNumber] = 1;
                    }
                }
            }
        }

        // The winner is the one to take the last stone
        // n is the number of stones and we've iterated to the end of n in the for loop
        // move alternately
        // If this is true meaning that it exists and at this position
        // Stan would win as he would be the last pick
        if(cache[n] == 1){
            // Stan always starts
            printf("Stan wins\n");
        } else {
            printf("Ollie wins\n");
        }
    }
}
