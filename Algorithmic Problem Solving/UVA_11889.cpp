/*
- Benefit
- Time Limit:5000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <stdio.h>
#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
using namespace std;

// Cool solution to avoid edge cases **

/*
// Iterative Version
int gcd(int a, int b){
    int c = 1;
    while(a != 0){ c = a; a = b%a; b = c; }
    return b;
}
*/

// Recursive Version
int gcd(int a, int b){ if(a == 0){ return b; } return gcd(b % a, a); }

// Eclid's Algorithm
int lcm(int a, int b){ return (a * b)/(gcd(a, b)); }

int main(){
    // the number of tests
    int T = 0;
    scanf("%d", &T);
    // Yaghoub is playing a new trick to sell some more
    // asks for B Tomans such that lowest common multiple of A and B equals to C and he will pay back a round bill
    int A = 0, C = 0, finalAnswer = 0, currentSolution = 0;
    // answerExists
    bool answerExists = false;
    while(T--){
        A = 0, C = 0, finalAnswer = 0, currentSolution = 0;
        answerExists = false;
        // Each test that comes in a separate line contains two integers A and C
        // ** PARSING
        scanf("%d %d", &A, &C);
        // Print the lowest integer B such that LCM(A, B) = C in a single line
        // Does answer exist?
        /*if(A == 0 && C == 0){
            // Well you can't have a least common divisor with 0, but for the sake of pointing this point
            printf("0\n");
            answerExists = false;
        } else*/ if(C % A != 0){
            // I guess to make it faster..
            printf("NO SOLUTION\n");
            answerExists = false;
        } else {
            answerExists = true;
        }
        // This probably won't work, but lets try
        // ** SOLVING
        // finalAnswer = C/A;
        // ** Print Answer
        // ** if the answer isn't correct
        // We detect this by solving the algorithm
        finalAnswer = C/A;
        // Solves Answer
        // This doesn't process if they are not edge cases
        // because this will save us time
        //if(!(lcm(A, finalAnswer) == C) && answerExists){ // **
        // We try solve this problem iteratively now
        // Stupid to run both lcm then again ...
        if(answerExists){
            // Also: improvement to improve each answer by the same proprtion
            // as C/A as that's the difference
            // for(int B = finalAnswer; B <= C; B += finalAnswer){
            while(!(lcm(A, finalAnswer) == C)){
                // We literally go through each case
                // for every possible value of b
                // Prime Factorization
                finalAnswer = ((C * gcd(A, finalAnswer)) / (A));
                // b is literally the subsitution for B
                // and if the Least Common Muliple is C then we know that the answer we have solved is B because it now matches.
            }
        }
        if(answerExists){printf("%d\n", finalAnswer);}
        // ** DEBUGS
        // We have to consider some edge cases
        // PROBLEM CASE 1: 357 111384
        //if(!(lcm(A, finalAnswer) == C) && answerExists){
        //    printf("The Problem Case is %d %d\n", A, C);
        //}
    }
    return 0;
}
