/*
* Where is the Marble?
* Time Limit:3000MS
* Memory Limit:0KB
* 64bit IO Format:%lld & %llu
- I have 3000MS so O(N*M + N) can do:
- Total no of test cases is less than 65
- Numbers are greater than 10000
*/

#include <stdio.h>
#include <algorithm>
using namespace std;

int main(){
    int N, M;
    int caseNumber = 1;
    while(scanf("%d %d", &N, &M) != EOF){
        // O(N)
        if(N == 0 && M == 0){
            break;
        }
        int marbles[10020];
        int i, x, k, currentNumber, location = 0;
        bool numberIsFound = false;
        numberIsFound = false;
        // Initialize Marble Array for the game
        printf("CASE# %d:\n", caseNumber);
        for(i = 0; i < N; i++){
            scanf("%d", marbles + i);
        }
        // Sort by the numbers so we can do a liner search
        sort(marbles, marbles + N);
        // O(N * M)
        for(x = 0; x < M; x++){
            scanf("%d", &currentNumber);
            for(k = 0; k < N; k++){
                if(marbles[k] < currentNumber){
                    location++;
                }
                if(marbles[k] == currentNumber){
                    numberIsFound = true;
                }
            }
            location++;
            if(numberIsFound){ printf("%d found at %d\n", currentNumber, location); }
            else{ printf("%d not found\n", currentNumber); }
            currentNumber = location = 0;
            numberIsFound =  false;
        }
        // Increment the case number
        caseNumber++;
    }
    return 0;
}
