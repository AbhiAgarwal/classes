/*
- Chest of Drawers
- Time Limit:1000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- solve() function inspired from Hints
Error Log:
- IT uses LONG.
- Completely forgot about the 65th case:: Change to 66
- Mistakenly forgot to print out the case where the answer is 0
*/
#define MAX 66

#include <algorithm>
#include <iostream>
#include <string.h>
#include <stdio.h>

using namespace std;
// bound: MAX, bound: MAX, bound: [0, 1]
long long solveArray[MAX][MAX][2];
/* Recurrence Equation:
    solve = BOUNDS:(n, s, [1, 0]){
        return 1 if n=0 s=0
        return 0 if n<0 s<0 s>n
        return cache_version if there
        return solve(0, n - 1, s - 1) + solve(1, n - 1, s) if L=1
        return solve(0, n - 1, s) + solve(1, n - 1, s) if L=0
    }
*/

// L = 1, 0 depending on locked/safe or not
// n = total number of drawers
// s = number of drawers that needs to be secured
// Counts the number of valid configuration using drawers [1, n] and requiring exactly s safe drawers with L as above
// The Array configuration is [n][s][L] for standard
long long solve(int L, int n, int s){
    /*
        At the beginning please notice that the top L should be the one locked.
        Because initially we imagine as the safe above the current one to be a hypothetical drawer that is already secure, and so L we will assume will be 0 for the drawers that are safe.
    */
    // Copying Brett's Memorization Techniques for Dynamic Programming
    if(s > n || s < 0 || n < 0){
        // Error Case:: We have either recieved bad results or have come too far, OR the values of s and n are lower than 0
        // if number of drawers that needs to be secured > total number of drawers, or negative drawers, locks to be done (LAST CASE!)
        return 0;
    } else if(n == 0 && s == 0){
        // Base Case:: Return the values straight up, bottom
        // As both n and s are 0:: meaning: total number of drawers, number of drawers that needs to be secured = 0 THEN there is only one way to solve this equation
        // If we don't set this to 1 then it won't be able to recurse back up properly
        return solveArray[n][s][L] = 1;
    } else if(solveArray[n][s][L] != -1){
        // Cache Case:: To see if we have the value in store, if not then ignore it
        return solveArray[n][s][L];
    } else {
        // Now we get to the recursive case which we care about the most
        // We need to think about cases where L is either LOCKED or when L safe
        if(L == 0){
            // This is the case where L == 0
            // L == 0 Meaning that the previous DRAWER was safe
            // Case One: Decrement the number of safes and keep the number you have to keep constant
            // Case Two: Decrement the number of saves BUT keep the number you have to keep constant: This is because you're going to make this one SAFE.
            // I was doing the s - 1 VERY INCORRECTLY. I was doing it for the L == 1 method because I thought that you had to use it whenever
            return solveArray[n][s][L] = solve(0, n - 1, s - 1) + solve(1, n - 1, s);
        } else {
            // Typical Seigel Method of splitting the dividing
            // L == 1 Meaning that the previous DRAWER was not safe
            // Case One: Decrease Drawers by one and the number of safe by one as it is already open and not safe, AND make next one not safe
            // When it is already zero you can decrease the value because it won't exactly make a big difference ::: Without these you will get 0 though as you've to use these to alter the Base Case.
            // Case Two: Make it safe, and decrement the number of safes by one so it limits the number of non-safe you can have
            return solveArray[n][s][L] = solve(0, n - 1, s) + solve(1, n - 1, s);
        }
    }
}

// Shortest main ever!!
int main(){
    int n, s = 0;
    while(scanf("%d %d", &n, &s) != EOF){
        // Start the recurrences
        memset(solveArray, -1, sizeof(solveArray[0][0][0]) * MAX * MAX * 2);
        if(n >= 0 && s >= 0){
            long long returnValue = solve(0, n, s);
            printf("%llu\n", returnValue);
        }
    }
    return 0;
}
