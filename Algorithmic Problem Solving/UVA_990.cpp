/*
- Diving for Gold
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Implementation help from: http://cs.nyu.edu/courses/spring14/CSCI-UA.0480-004/HW4Hints.pdf, from Page 108 of the Book, and knapsack function from the "ch3_07_UVa10130.cpp" that came with the book
*/

#define MAX_N 1010
#define MAX_W 50

#include <stdio.h>
#include <algorithm>
#include <iostream>
#include <string>
#include <sstream>
#include <cstring>
using namespace std;

int W[MAX_N];
int V[MAX_N];
int cache[MAX_N][MAX_W];
int numberOfCoins = 0;
int t, w = 0;
int totalDepth = 0;

// Taken from homework hint
int value(int numberOfTreaures, int t){
    // Copying Brett's Memorization Techniques for Dynamic Programming
    if(numberOfTreaures < 0 || t < 0){
        // Error Case so we'll just return -1 and we can detect for this later.
        // We have already checked for this, but I want to make a stand-alone function
        return -1;
    } else if (numberOfTreaures == 0 || t == 0){
        // Base Case:: If either numberOfTreasures or the time is zero then we want to return 0 as the value would simply be 0
        return 0;
    } else if (cache[numberOfTreaures][t] != -1){
        // Cache Level:: If we have it stored then return it!
        return cache[numberOfTreaures][t];
    } else { // Group the main cases together
        int returnValue = -100; // What we need to return
        // Calculation Level:: Count down
        // From the book: 3rd Case, W[numberOfTreaures] > t
        // "We have no choice but to ignore this item"
        if(W[numberOfTreaures] > t){
            returnValue = value(numberOfTreaures - 1, t);
        } else {
        // Case where W[numberOfTreaures] <= t
        // We have more time to fix this problem that to not
        // Else: "Ignore or take this item; we take the maximum"
        // Depending:
        // Case One: -1 to treasures, and keep constant time:: "val(id + 1, renW)", page 108. And we don't add a value because we're not literally adding this current one but the next one!
        // Case Two: -1 and add the value & decrease the time as it works:: "V[id] + val(id + 1, renW - W[id]", page 108
            int caseOne = value(numberOfTreaures - 1, t);
            int caseTwo = value(numberOfTreaures - 1, (t - W[numberOfTreaures])) + V[numberOfTreaures];
            //printf("%d %d\n", caseOne, caseTwo);
            returnValue = max(caseOne, caseTwo);
            // numberOfCoins analysis
            // If the case is a success:: It works > not
        }
        // Let us put it into the cache level access
        return cache[numberOfTreaures][t] = returnValue;
    }
}

// From the Hints from the homework & a mixture of my own things
int buildOut(int numberOfTreaures, int t, int w, int pos, string total[]){
    if(0 > pos){
        std::cout << totalDepth << "\n";
        for(int i = totalDepth; i >= 0; i--){
            std::cout << total[i];
        }
        return 0;
    }
    int left = t - W[pos];
    if(left >= 0 && ((value(pos - 1, left) + V[pos]) == value(pos, t))){
        int currentD = W[pos] / (3 * w);
        std::stringstream sb;
        sb << currentD <<  " " << V[pos] << "\n";
        total[totalDepth] = sb.str();
        totalDepth++;
        return buildOut(numberOfTreaures, left, w, pos - 1, total) + 1;
    } else {
        return buildOut(numberOfTreaures, t, w, pos - 1, total);
    }
}

// Read Write
int main(){
    int numberOfTreaures = 0;
    int i, x = 0;
    int largestValuation = 0;
    bool checkPrint = false;
    while(scanf("%d %d", &t, &w) == 2){
        // Checks if there is a new
        if(checkPrint){
            printf("\n");
        } else {
            checkPrint = true;
        }
        totalDepth = 0;
        memset(W, 0, sizeof(W));
        memset(V, 0, sizeof(V));
        // Set it to -1 as then we can easily check for it not being present
        memset(cache, -1, sizeof(cache[0][0]) * MAX_N * MAX_W);
        scanf("%d", &numberOfTreaures);
        // We are getting the information for V, W here
        for(i = 1; i < (numberOfTreaures + 1); i++){
            int depth, qualityOfGold = 0;
            scanf("%d %d", &depth, &qualityOfGold);
            // For sake of explicitness, and remembering
            // W is basically the ascent time + descent time
            // small w is the constant for accessing the acent and the descent
            W[i] = (w * depth) + (2 * w * depth);
            // Value of each treasure so we can know how much we need to maximize the amount we are bringing back up
            V[i] = qualityOfGold;
        }
        /* We need to maximize the value of our "bag", WE HAVE NO LIMIT BUT TIME. SO the limit in our situation will actually be TIME instead of space. We just need to restructure the problem. */
        // Main function to calculate the valuation
        string total[MAX_N];
        if(numberOfTreaures >= 0 && t >= 0){
            largestValuation = value(numberOfTreaures, t);
            if(largestValuation >= 0){
                printf("%d\n", largestValuation);
                buildOut(numberOfTreaures, t, w, numberOfTreaures, total);
            }
        }
    }

}
