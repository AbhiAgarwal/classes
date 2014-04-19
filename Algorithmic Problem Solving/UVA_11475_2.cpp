/*
- Extend to Palindrome
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
#include <sstream>
using namespace std;

#define MAX_N 100010

/*
    - we basically have to get how many values we take from the end of the string which would keep it a pallindrome
    - find the longest suffix of the string that is a prefix of the reverse using KMP
    - Procedure ->
        - build the KMP Table
        - search through the Table
        - get the value of how many we should reverse
        - from the end reverse the ones that are required
*/

// Taken from the book
char T[MAX_N], P[MAX_N], TA[MAX_N]; // T = text, P = pattern, TA = table

// http://cs.nyu.edu/courses/spring14/CSCI-UA.0480-004/Lecture20Examples/KMP.java
void buildKMPTable(int currentSize){
    for(int i = 2; i < currentSize; ++i){
        int j = TA[i - 1];
        do {
            if(P[j] == P[i - 1]){
                TA[i] = j + 1;
                break;
            } else {
                j = TA[j];
            }
        } while(j != 0);
    }
}

int simulate(int currentSize){
    int state = 0;
    //printf("%zu\n", strlen(P));
    for(int i = 0; i < currentSize; ++i){
        while (true){
            if (P[i] == T[state]){
                state++;
                break;
            } else if(state == 0){
                break;
            }
            state = TA[state];
        }
        if(state == currentSize){
            break;
        }
    }
    //printf("%d\n", state);
    return state;
}

int main(void){
    // memset(T, '\0', sizeof(char) * MAX_N);
    // Input will consist of several lines ending in EOF
    while(cin >> T){
        // Reset and set values to -1
        /* WIKIPEDIA NOTES ON TABLE & SEARCH
            - We consider the example of W = "ABCDABD" first. We will see that it follows much the same pattern as the main search, and is efficient for similar reasons.
            - We set T[0] = -1. To find T[1], we must discover a proper suffix of "A" which is also a prefix of W. But there are no proper suffixes of "A", so we set T[1] = 0. Likewise, T[2] = 0.
        */
        // TA = Table here NOT T
        memset(TA, 0, sizeof(char) * MAX_N);
        TA[0] = -1;
        //memset(P, 0, sizeof(char) * MAX_N);
        /* - It should contain the palindrome formed by adding the fewest number of extra letters to the end of the corresponding input string */
        int currentSize = strlen(T);
        // Build up the pattern array
        for(int i = (currentSize - 1), j = 0, inputValue = (i - j); j < currentSize; ++j, inputValue = (i - j)){
            P[inputValue] = T[j];
        }
        /*- Build the table */
        buildKMPTable(currentSize);
        /* - To do this, find the longest suffix of the string that is a prefix of the reverse using KMP, and then reverse */
        int appendAmount = simulate(currentSize);
        /*
            - Easier to go down than to take the reverse, and go down to 0 from where we should reverse from
            - for(int i = 0; i <= (appendAmount-1); ++i){ printf("%d\n", (currentSize/3) - i);}
            - We go down front a current amount rather than go up
            - Your solution should take as its input a string and produce the smallest palindrome. It can be formed by adding zero or more characters at its end
        */
        cout << T;
        if(appendAmount != 0){
            stringstream toAppend;
            for(int i = (appendAmount - 1); i >= 0; --i){
                toAppend << T[i];
            }
            cout << toAppend.str();
        }
        cout << "\n";
    }
}
