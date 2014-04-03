/*
- GCD
- Input: Standard Input
- Output: Standard Output
 */

#include <algorithm>
#include <cstdio>
#include <vector>
#include <queue>
#include <sstream>
#include <iostream>
using namespace std;

//Here GCD() is a function that finds the greatest common divisor of the two input numbers
// Euclid's algorithm for GCD
int GCD(int i, int x){
    if(x == 0){
        return i;
    } else {
        return GCD(x, i % x);
    }
}

int main(){
    // Given the value of N, you will have to find the value of G.
    // G = http://uva.onlinejudge.org/external/114/p11417.gif
    // GCD(i,j) means the greatest common divisor of integer i and integer j
    long long currentValue = 0;
    int N = 0;
    while(scanf("%d", &N) != EOF){
        // if N < 0 then return
        // Input is terminated by a line containing a single zero.
        if(N <= 0){
            break;
        }
        // Initialization
        currentValue = 0;
        // summation notation, a function of G
        for(int i = 1; i < N; i++){
            for(int x = (i + 1); x <= N; x++){
                currentValue += GCD(i, x);
            }
        }
        // Print out the LONG LONG INT value
        printf("%lld\n", currentValue);
    }
}
