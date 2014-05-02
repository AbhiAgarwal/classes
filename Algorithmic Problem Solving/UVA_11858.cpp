/*
- Frosh Week
- Time Limit:8000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Mistake: LONG LONG..
*/

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
#include <sstream>
#include <queue>
#include <math.h>
#include <map>
using namespace std;
long long numberOfSwaps = 0;

/* Merge Elements */
void merge(vector<long long> &student, int a, int b){
    int mid = floor((a + b) / 2), x = 0, y = a, z = mid + 1;
    int current[b - a + 1], currentCount = mid - a + 1;
    while(y <= mid && z <= b){
        if(student[y]<student[z]){current[x++] = student[y++]; currentCount--;}
        else{current[x++] = student[z++]; numberOfSwaps += currentCount;}
    }
    while(y <= mid){current[x++] = student[y++];}
    while(z <= b){current[x++] = student[z++];}
    for(int i = a; i <= b; i++){student[i] = current[i-a];}
}

/* Run Merge Sort */
void mergeSort(vector<long long> &studentNumbers, int a, int b){
    if(a < b){ int mid = floor((a + b) / 2);
        mergeSort(studentNumbers, a, mid);
        mergeSort(studentNumbers, mid + 1, b);
        merge(studentNumbers, a, b);
    }
}

/* Parse and Run */
int main(void){
    int n = 0; long long currentNumber = 0;
    while(scanf("%d", &n) == 1){
        currentNumber = 0; vector<long long> studentNumbers; numberOfSwaps = 0;
        for(int i = 0; i < n; ++i){ scanf("%lld", &currentNumber);
            studentNumbers.push_back(currentNumber);
        } mergeSort(studentNumbers, 0, n - 1);
        printf("%lld\n", numberOfSwaps);
    }
}
