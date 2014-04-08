/*
- Benefit
- Time Limit:5000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Finding quickest GCD
*/

#include <stdio.h>
#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
#include <iostream>
using namespace std;

/*int gcdIterative(int a, int b){int c = 1; while(a != 0){c = a; a = b%a; b = c;} return b;}*/
/*long gcd(long a, long b){return (b != 0)?gcd(b, a%b):a;}*/
/*long gcd(long a, long b){while(b) b ^= a ^= b ^= a %= b;return a;}*/

long gcd(long x, long y) {
    while(x * y != 0){if(x >= y){x = x % y;}else{y = y % x;}}
    return (x + y);
}

long lcm(long a, long b){return abs(a * b)/(gcd(a, b));}

int main(){
    int T = 0;
    long A = 0, C = 0, finalAnswer = 0;
    cin >> T;
    while(T--){
        cin >> A >> C;
        if(C % A != 0){printf("NO SOLUTION\n");} else {
            // Prime Factorization to reduce time
            finalAnswer = (C/A);
            while(lcm(A, finalAnswer) != C){
                finalAnswer += (C/A);
            }
            cout << finalAnswer << "\n";
        }
    }
    return 0;
}
