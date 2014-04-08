/*
- Tennis contest
- Time Limit:1000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
#include <iostream>
#include <math.h>
using namespace std;

/*
unsigned int factorial(unsigned int x){
    unsigned int val = 1;
    for(;x > 1; --x){val *= x;}
    return val;
}
unsigned int binCof(unsigned int n, unsigned int k){
    unsigned int val = 1;
    // Multiplicative formula - individual binomial coefficients
    for(int i = 1; i <= k; i++){
        val *= (((n + 1) - i) / i);
    }
    return val;
    //return factorial(n) / (factorial(k) * factorial(n-k));
}
*/

double binomial_coefficient(unsigned int k, unsigned int r, double p) {
    int i;
    double b;
    //if (0 == k || n == k){return 1;}
    //if (k > n){return 0;}
    //if (k > (n - k)){k = n - k;}
    //if (1 == k){return n;}
    b = p;
    // Equations from wikipedia & book
    for (i = 0; i < (k-1); ++i) {
        b*=(k - 1 + (r - i));
        if (b < 0) return -1; /* Overflow */
        b/=(i+1);
        b*=p; // pow(p, k); right hereeeeeeee
    }
    return b;
}

double nbpmf(unsigned int k, unsigned int r, double p){
    double toMultiply = binomial_coefficient(k, r, p);
    // Go until r - 1
    //for(int i = 0; i < (r - 1); ++i){
    //    toMultiply *= binomial_coefficient(i + r - 1, k);
    //}
    return toMultiply * pow((1.0 - p), (double)r);
}

int main(){
    // t, indicating the number of test cases
    int t = 0;
    scanf("%d", &t);
    // n = representing the number of wins A has to reach
    // p = representing the probability of A to win a match
    double n = 0, p = 0, toReturn = 0;
    while(t--){
        // Reset Values
        toReturn = 0;
        // Get Values
        scanf("%lf", &n);
        scanf("%lf", &p);
        // single line with the number representing the probability in advance of A to win the title of best player in the world
        for(int i = 0; i < n; ++i){
            toReturn += nbpmf(n, i, p);
        }
        printf("%.02lf\n", toReturn);
    }
}
