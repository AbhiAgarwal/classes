/*
- Complex Numbers
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- De Moivre's formula
*/

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
#include <ctype.h>
#include <cmath>
#include <complex>
using namespace std;

#define PI acos(-1.0)
#define EPS 1e-9
#define error 5e-4

struct complexNumbers {
    double x, y;
    complexNumbers() : x(0), y(0) {}
    complexNumbers(double _x, double _y) : x(_x), y(_y) {}
};

// Sort
struct complexSort{
    bool operator()(const complexNumbers& lx, const complexNumbers& rx) const {
        double rone = lx.x, rtwo = rx.x, ione = lx.y, itwo = rx.y;
        /* They don't exactly have to be the same */
        if(abs(rone - rtwo) < EPS){ return ione > itwo; }
        else { return rone > rtwo; }
    }
};

int main(void){
    int a = 0, b = 0, caseNumber = 0, n = 0; char operand, i;
    while(cin >> a >> operand >> b >> i >> n){
        if(caseNumber != 0){cout << endl;}
        /* Parse & OUTPUT */
        caseNumber++; cout << "Case " << caseNumber << ":" << endl;
        if(operand == '-'){b = -b;} /* Conversion of negative */
        /* FOR SORTING */
        vector<complexNumbers> allPoints = vector<complexNumbers>();
        /* PROCESSING */
        double r = sqrt(pow(a, 2.0) + pow(b, 2.0)), angle = atan2(b, a);
        double powValue = pow(r, (1 / (double)n));
        for(int k = 0; k <= (n - 1); ++k){ //for k = 0, 1, 2, 3, ..., n - 1
            // Cauclating nth root for current k value
            double cosValue = cos((1 / (double)n) * (angle + 2 * PI * k));
            double sinValue = sin((1 / (double)n) * (angle + 2 * PI * k));
            // X and Y value for the current model.
            double oneNumber = powValue * cosValue, twoNumber = powValue * sinValue;
            // Data cleanup
            // We are trying to use the fact that the value must have less than the standard error here to not be rounded up
            // Negative zero problem
            if(oneNumber <= 0 && oneNumber > -error){ oneNumber = 0.0; }
            // Standard error is less than 'error' because if it was equal to 'error' then it would round up to 0.00x
            // We are printing out 3 decimal places so we need to pick a value that is e-4, which is one above. Anything above or equal to 5e-4 will be rounded up to 0.00x.
            // We pick the number 5e-4 so if it is less than then we can round it down to zero.
            // They both can be needed to clean up...
            if(twoNumber <= 0 && twoNumber > -error){ twoNumber = 0.0;}
            // Formulation of complexNumber struct and appending
            complexNumbers currentNumber = complexNumbers(oneNumber,twoNumber);
            allPoints.push_back(currentNumber);
        }
        /* SORT */
        std::sort(allPoints.begin(), allPoints.end(), complexSort());
        /* OUTPUT */
        for(auto eachComplexNumber : allPoints){
            // Data OUTPUT
            if(eachComplexNumber.y >= 0){
                printf("%.3lf+%.3lfi\n", eachComplexNumber.x, eachComplexNumber.y);
            } else {
                printf("%.3lf%.3lfi\n", eachComplexNumber.x, eachComplexNumber.y);
            }
        }
    }
    printf("\n");
}
