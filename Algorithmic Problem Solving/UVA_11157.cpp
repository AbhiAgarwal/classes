/*
* Dynamic Frog
* Time Limit:3000MS
* Memory Limit:0KB
* 64bit IO Format:%lld & %llu
* Greedy Algorithm
* It would be atleast N^2
*/

#include <algorithm>
#include <cstdio>
#include <iostream>
#include <cstring>
using namespace std;

/*
    3
    1 10
    B-5
    1 10
    S-5
    2 10
    B-3 S-6
*/

// Here we are going to always be initializing the first and last to be the greatest possible distance between them, and of the largest size.

// We have declare both the size & dimensions inside
// because we don't know the numberOfStones beforehand.
int main(){
    int numberOfCases, currentCase = 0;
    // Getting number of Cases
    scanf("%d", &numberOfCases);
    // Going through each case
    while(numberOfCases--){
        currentCase++;
        // Reset of the values &
        // Initialization of the Variables
        int numberOfStones, distance = 0;
        int i, x = 0;
        int maximumLeap = 0; // main return
        char letter[200]; // Character Array
        int digit[200]; // Stones
        // Get to the real work
        scanf("%d %d", &numberOfStones, &distance);
        numberOfStones += 1;
        // Now for the main event
        /*
        The point is: B = 2 steps, S = 1 step.
        */
        // Here we are getting all the values
        for(i = 1; i < numberOfStones; i++){
            scanf("%1s-%d", &letter[i], &digit[i]);
        }
        // We want to start at index 1, and we need
        // to fill the first and last so we can determine
        // a distance in the long run
        // The first & and the last have to be a maximized leap
        digit[0] = 0; letter[0] = 'B'; // set first one to be B
        digit[numberOfStones] = distance; letter[numberOfStones] = 'B'; // and last one
        for(x = 1; x <= numberOfStones;){
            int useCase = x; // Required to alter x at any point
            int twoCase = x + 1;
            int threeCase = x + 2;
            // Last Case: Reach the end
            if(x <= numberOfStones){
                int caseOne = digit[x] - digit[x - 1];
                maximumLeap = max(maximumLeap, caseOne);
                useCase = x + 1;
            }
            // Second case: One/Two Leaps when it isn't B
            // We are going to attempt to move 2 forward to
            // make up for ONE SINGLE BIG STONE
            if((twoCase <= numberOfStones) && (letter[x] != 'B')){
                int caseTwo = digit[x + 1] - digit[x - 1];
                maximumLeap = max(maximumLeap, caseTwo);
                useCase = x + 2; // we are two forward
            }
            if((threeCase <= numberOfStones) && (letter[x] != 'B') && (letter[x + 1] != 'B')){
                useCase--; // Next isn't B so it might
                // While the current POSITION is not a big stone
                while(letter[useCase] != 'B'){
                    useCase++;
                    int caseThree = digit[useCase] - digit[useCase - 2];
                    maximumLeap = max(maximumLeap, caseThree);
                }
                useCase++; // Go one forward
            }
            // We want to update x right.
            x = useCase;
        }
        printf("Case %d: %d\n", currentCase, maximumLeap);
    }
}
