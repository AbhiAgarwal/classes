/*
* Garbage Heap
* Time Limit:3000MS
* Memory Limit:0KB
* 64bit IO Format:%lld & %llu
*/
#define MAX_ABC 25 // (1 <= A, B, C <= 20)
#include <stdio.h>
#include <algorithm>
#include <iostream>
#include <string>
#include <sstream>
#include <cstring>
#include <cmath>
#include <limits>
using namespace std;
/* Notes:
- One Indexing seems easier here.
- Resources: Lecture 8 (http://cs.nyu.edu/courses/spring14/CSCI-UA.0480-004/Lecture8.pdf), Book
*/
long long sum[MAX_ABC][MAX_ABC][MAX_ABC];
// Solutions
long long finalAnswer = 0;
int A, B, C = 0;

void Kadane(){
    long long maximumSum = std::numeric_limits<int>::min();
    // Now we get to the real operations:: O(n^5)
    // Hint: "loop over all possible opposing corners of base rectangle"
    // This is the max rectangle sum where 0's are treated as
    for(int b1 = 1; b1 < B; b1++){
        for(int c1 = 1; c1 < C; c1++){
            for(int b2 = b1; b2 < B; b2++){
                for(int c2 = c1; c2 < C; c2++){
                    // Start Kadane's Algorithm
                    // currentSum is set to the lowest possible value
                    long long currentSum = 0;
                    for(int a = 1; a < A; a++){
                        // getRectSum implementation
                        currentSum += sum[a][b2][c2] - sum[a][b2][c1 - 1] - sum[a][b1 - 1][c2] + sum[a][b1 - 1][c1 - 1];
                        // calculate max ending here
                        maximumSum = max(maximumSum, currentSum);
                        // calculate max so far
                        currentSum = max(0LL, currentSum);
                    }
                }
            }
        }
    }
    finalAnswer = maximumSum;
}

int main(){
    int numberOfTestCases = 0;
    scanf("%d", &numberOfTestCases); // ie: 1
    while(numberOfTestCases--){
        // Reset
        A = B = C = finalAnswer = 0;
        // Scan
        scanf("%d %d %d", &A, &B, &C); // ie: 2 2 2
        // Firstly we input the whole, this section works (TESTED):: O(n^3)
        A++;B++;C++; // so we can get the indexing
        memset(sum, 0, sizeof(sum[0][0][0]) * MAX_ABC * MAX_ABC * MAX_ABC);
        for(int i = 1; i < A; i++){
            for(int x = 1; x < B; x++){
                for(int y = 1; y < C; y++){
                    // The integers we put in must be long long integers or %lld
                    long long currentValue = 0;
                    scanf("%lld", &currentValue); // ie: -1 2 0 -3 -2 -1 1 5
                    // Getting the sum[][][] values
                    sum[i][x][y] += currentValue + sum[i][x - 1][y] + sum[i][x][y - 1] - sum[i][x - 1][y - 1];
                }
            }
        }
        // Processes Answer
        Kadane();
        printf("%lld\n", finalAnswer); // Global Answer
        if((numberOfTestCases > 0)){printf("\n");}
    }
}
