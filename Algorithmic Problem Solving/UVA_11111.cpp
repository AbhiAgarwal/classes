/*
- Generalized Matrioshkas
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
#include <queue>
#include <math.h>
#include <map>
#include <stack>
using namespace std;

int main(void){
    string eachLine;
    while(getline(cin, eachLine)){
        /* INPUT */
        istringstream eachInput(eachLine); bool outputCheck = true;
        int eachNumber = 0; stack<int> values = stack<int>();
        /* PROCESSING */
        // Edges: -50 -20 20 -15 15
        while(eachInput >> eachNumber){
            if(eachNumber < 0){ values.push(eachNumber); }
            else {
                // Move until it works
                int currentSum = 0;
                while(values.size() > 0 && values.top() != -eachNumber){
                    int value = values.top();values.pop();currentSum += value;
                    if(value < 0){ // Is Negative
                        outputCheck = false; break;
                    } else if(values.size() == 0){ // Size is now 0
                        outputCheck = false; break;
                    }
                }
                // Check if correct
                if(values.size() == 0){
                    outputCheck = false; break;
                } else if(currentSum >= eachNumber){
                    outputCheck = false; break;
                } else if(!outputCheck){
                    outputCheck = false; break;
                } else {
                    values.pop(); values.push(eachNumber);
                }
            }
        }
        // Filter out wrong results
        while(values.size() > 0){
            if(values.top() < 0){ outputCheck = false; break; } values.pop();
        }
        /* OUTPUT */
        if(outputCheck){ printf(":-) Matrioshka!\n"); }
        else { printf(":-( Try again.\n"); }
    }
}
