/*
- Jimmy's Balls
- Time Limit:1000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
using namespace std;

int main(){
    // Each case is an integer N
    // N denotes the total number of balls in the bag
    int N = 0, caseNumber = 0;
    long long finalAnswer = 0;
    // Input is terminated with a 0
    while(scanf("%d", &N)){
        finalAnswer = 0;
        caseNumber++;
        // Input is terminated with a 0
        if(N == 0){break;}
        // Each ball has a single color. There are three colors; red, blue and green.
        // how many different colored combinations can there be
        int currentColor = 0;
        // Increment the number of colors for each iteration
        while(++currentColor){
            // printf("%d\n", currentColor);
            // If we are dropping in colors
            /*
            -> N = total number of balls in the bag
            -> currentColor = current number of balls
            - Come up with a formula to solve the problem when there are only two colors
            - 2 color formula is N - (2 * currentNum + 1) > currentNum + 1
            */
            if((N - (2 * currentColor + 1)) > (currentColor + 1)){
                // add the formulation for 3 color case
                finalAnswer += ((N - 1 - ((3 * currentColor))) / 2);
            } else {
                // Break if we don't need to consider anymore cases
                break;
            }
        }
        printf("Case %d: %lld\n", caseNumber, finalAnswer);
    }
}
