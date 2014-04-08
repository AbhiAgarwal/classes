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

int gcdIterative(int a, int b){int c = 1; while(a != 0){c = a; a = b%a; b = c;} return b;}
int gcd(int a, int b){return (b != 0)?gcd(b, a%b):a;}
int lcm(int a, int b){return (a * b)/(gcd(a, b));}
int main(){
    int T = 0, A = 0, C = 0, finalAnswer = 0; scanf("%d", &T);
    while(T--){ scanf("%d %d", &A, &C);
        if(C % A != 0){printf("NO SOLUTION\n");} else { finalAnswer = (C/A);
            while(lcm(A, finalAnswer) != C){finalAnswer = ((C * gcdIterative(A, finalAnswer)) / (A));}
            printf("%d\n", finalAnswer);
        }
    }
    return 0;
}
