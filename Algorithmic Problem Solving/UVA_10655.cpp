/*
- Contemplation! Algebra
- Input: Standard Input
- Output: Standard Output
- Time Limit: 1 Second
*/

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
using namespace std;

int main(void){
    // 'non-negative' -> hence 'signed'.
    signed long p = 0, q = 0, n = 0; // 'signed 32-bit integer'
    signed long long finalAnswer = 0; // 'signed 64-bit integer'
    while(cin >> p >> q){
        /* PARSING */
        if(p == 0 && q == 0){ break; } else { cin >> n; }

        /* PROCESSING */
        // p = a + b, q = a * b, n = power to raise (a^n + b^n)

        /* OUTPUT */
        cout << finalAnswer << endl;
    }
}
