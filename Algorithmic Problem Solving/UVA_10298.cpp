/*
- Power Strings
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
using namespace std;

/*
    - Split
    - Compare
*/

int main(void){
    // Each test case is a line of input representing s, a string of printable characters
    string currentTestCase = "";
    int size = 1, currentSize = 0;
    // The length of s will be at least 1 and will not exceed 1 million characters
    while(cin >> currentTestCase){
        if(currentTestCase == "."){break;}
        // For each s you should print the largest n such that s = a^n for some string a
        size = 1, currentSize = currentTestCase.length();
        // printf("%d\n", currentSize);
        for(int i = 1; i <= currentSize; ++i){
            // If we can divide the chunk
            // a^(n+1) = a*(a^n).
            // We are basically splitting up the files here
            // and if we can divide them equally using the mod function then it means we can split them for comparison
            if(currentSize % i == 0){
                // each s you should print the largest n such that s = a^n for some string a
                // Same approach as Floyd Warshall
                string currentStringSPLIT1 = currentTestCase.substr(0, currentSize - i);
                string currentStringSPLIT2 = currentTestCase.substr(i, currentSize);
                // Check if the strings we have split are equal
                // If they are then there exists this solution
                // if not then it there doesn't
                if(currentStringSPLIT1 == currentStringSPLIT2){
                    size = (currentSize/i);
                    break;
                }
            }
        }
        printf("%d\n", size);
    }
}
