/*
- What Goes Up
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Source: Book & Hint
- Forgot to include <cstring> oops! not <string>!!
*/
#define MAX_V 150000

#include <stdio.h>
#include <algorithm>
#include <iostream>
#include <vector>
#include <stack>
#include <cstring>
using namespace std;

int longSub[MAX_V], L[MAX_V], L_id[MAX_V], P[MAX_V];
int longSubLength = 0, longSubSize = 0, longSubSizeEnd = 0;

// Reconstructs an array to print
// Code sampled & learned from the book notes and notes
void arrayConstruct(int end, int a[], int p[]) {
    int x = end;
    stack<int> s;
    for (; p[x] >= 0; x = p[x]){
        s.push(a[x]);
    }
    printf("%d\n", a[x]);
    for (; !s.empty(); s.pop()){
        printf("%d\n", s.top());
    }
}

// Performs Modified Binary Search on the values
// Code sampled & learned from the book notes and notes
void binarySearch(){
    for (int i = 0; i < longSubLength; ++i){
        int pos = lower_bound(L, L + longSubSize, longSub[i]) - L;
        L[pos] = longSub[i];
        L_id[pos] = i;
        P[i] = pos ? L_id[pos - 1] : -1;
        if (pos + 1 > longSubSize) {
            longSubSize = pos + 1;
            longSubSizeEnd = i;
        }
    }
}

// Parse & Call LIS
// Code sampled & learned from the book notes and notes
int main(){
    // Parsing through the numbers
    memset(longSub, -1, sizeof(longSub[0]) * MAX_V);
    memset(L, -1, sizeof(longSub[0]) * MAX_V);
    memset(L_id, -1, sizeof(longSub[0]) * MAX_V);
    memset(P, -1, sizeof(longSub[0]) * MAX_V);
    int iterator = 0;
    while(!cin.eof()){
        cin >> longSub[iterator]; iterator++;
    }
    longSubLength = iterator;
    // Returns the number of values
    binarySearch();
    printf("%d\n", longSubSize);
    printf("-\n");
    // Reconstructs the array
    arrayConstruct(longSubSizeEnd, longSub, P);
}
