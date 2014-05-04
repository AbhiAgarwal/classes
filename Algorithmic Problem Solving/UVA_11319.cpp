/*
- Stupid Sequence
- Time Limit:1000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#define MAX_N 7

#include <cmath>
#include <cstdio>
#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
using namespace std;

struct AugmentedMatrix{
    double mat[MAX_N][MAX_N + 1];
    AugmentedMatrix(){
        memset(mat, 0, sizeof(mat[0][0]) * MAX_N * (MAX_N + 1));
    }
    void adding(int INDEX, int a, int b, int c, int d, int e, int f, int g){
        mat[INDEX][0] = a; mat[INDEX][1] = b; mat[INDEX][2] = c;
        mat[INDEX][3] = d; mat[INDEX][4] = e; mat[INDEX][5] = f;
        mat[INDEX][6] = g;
    }
};
struct ColumnVector{
    double vec[MAX_N];
    ColumnVector(){
        memset(vec, 0, sizeof(vec[0]) * MAX_N);
    }
};

// Taken from book
ColumnVector GaussianElimination(int N, AugmentedMatrix Aug){
    int i, j, k, l; double t;
    for(i = 0; i < N - 1; i++){
        l = i;
        for(j = i + 1; j < N; j++){
            if(fabs(Aug.mat[j][i]) > fabs(Aug.mat[l][i])){
                l = j;
            }
        }
        for (k = i; k <= N; k++){
            t = Aug.mat[i][k],
            Aug.mat[i][k] = Aug.mat[l][k],
            Aug.mat[l][k] = t;
        }
        for (j = i + 1; j < N; j++){
            for (k = N; k >= i; k--){
                Aug.mat[j][k] -= Aug.mat[i][k] * Aug.mat[j][i] / Aug.mat[i][i];
            }
        }
    }
    ColumnVector Ans;
    for (j = N - 1; j >= 0; j--){
        for (t = 0.0, k = j + 1; k < N; k++){
            t += Aug.mat[j][k] * Ans.vec[k];
        }
        Ans.vec[j] = (Aug.mat[j][N] - t) / Aug.mat[j][j];
    }
    return Ans;
}

int main(void){
    AugmentedMatrix seriesOfEquations = AugmentedMatrix();
    seriesOfEquations.adding(0, 0, 0, 0, 0, 0, 0, 0);
    seriesOfEquations.adding(1, 0, 0, 0, 0, 0, 0, 0);
    seriesOfEquations.adding(2, 0, 0, 0, 0, 0, 0, 0);
    seriesOfEquations.adding(3, 0, 0, 0, 0, 0, 0, 0);
    seriesOfEquations.adding(4, 0, 0, 0, 0, 0, 0, 0);
    seriesOfEquations.adding(5, 0, 0, 0, 0, 0, 0, 0);
    seriesOfEquations.adding(6, 0, 0, 0, 0, 0, 0, 0);


    ColumnVector toReturn = GaussianElimination(7, seriesOfEquations);
}
