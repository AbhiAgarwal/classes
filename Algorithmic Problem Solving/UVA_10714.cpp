/*
* Ants
* Time Limit:3000MS
* Memory Limit:0KB
* 64bit IO Format:%lld & %llu
- Simple question as the case is just going at 1cm/s
- So the time will be just the distance - ant location
*/

#include <stdio.h>
#include <algorithm>
using namespace std;

int main(){
    int numberOfCases, poleSize, numberOfAnts, timeEarliest, timeLast, i, currentAnt;
    scanf("%d", &numberOfCases);
    while(numberOfCases--){ // going through each case
        poleSize = i = currentAnt = numberOfAnts = timeLast = 0; // Reset Variables
        scanf("%d %d", &poleSize, &numberOfAnts); // Actual Start
        while(numberOfAnts--){ // Going through each ant iteration
            scanf("%d", &currentAnt); // Getting ant
            int current = poleSize - currentAnt;
            timeEarliest = max(timeEarliest, min(currentAnt, current));
            timeLast = max(timeLast, max(currentAnt, current));
        }
        printf("%d %d\n", timeEarliest, timeLast);
    }
    return 0;
}
