/*
- Base i-1
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Change in base formulation
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

// Takes an int and appends it to a string
void appendInt(int number, string &toAppend){
    stringstream ss; ss << number; toAppend.append(ss.str());
}

// Strings are mutable so this is simple :)
void baseIOne(int a, int b){
    string toAppend = "";
    // input
    // a = 1, b = 0
    // We have to convert a number INTO BASE X
    // if X is a positive number GREATER THAN OR EQUAL TO 2
    while(a != 0 || b != 0){
        bool isOdd = false;

        /* NUMBERS TO APPEND */
        // Filter which one to print out
        // IF IT IS EVEN
        if((a + b) % 2 == 0){appendInt(0, toAppend);}
        // IF IT IS ODD THEN WE REMOVE ONE
        else{ appendInt(1, toAppend); isOdd = true; }

        /* ALTER FOR NEXT INPUT */
        // We are trying to find the REAL part of the equation
        // and this will help us do it
        // beforeChange == basically "a" before we change it..
        if(isOdd){a--;} int beforeChange = a;
        // We are changing bases so we're dividing it by 2 in each step.
        // We have to do the same thing we do for change in bases formulation.
        // b - a and b + a
        // So we append a number and remove one.
        a = ((b - a) / 2); b = -1 * ((b + beforeChange)/ 2);
    }
    // Reverse String like we do for change in base formulation
    cout << (string(toAppend.rbegin(), toAppend.rend()));
}

// Parse
int main(void){
    int numberOfCases = 0, currentCase = 0, a = 0, b = 0; cin >> numberOfCases;
    while(numberOfCases--){
        currentCase++; cin >> a >> b; cout << "Case #" << currentCase << ": ";
        if(a == 0 && b == 0){cout << 0 << endl;}
        else{baseIOne(a, b); cout << endl;}
    }
}
