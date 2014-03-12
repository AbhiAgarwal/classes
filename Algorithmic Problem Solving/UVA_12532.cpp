/*
- Interval Product
*/

// How can one forget stdio.h for any reason..
#define MAX_V 1<<18
#include <iostream>
#include <cstring>
#include <stdio.h>
using namespace std;

class Fenwick{
public:
    static const int MAX_SIZE = 1<<18;
    int fen[MAX_SIZE];
    void adjust(int i, int adj){
        while (i < MAX_SIZE){
            fen[i] += adj;
            i += (i & (-i));
        }
    }
    int sumQuery(int k){
        int ret = 0;
        while(k > 0){
            ret += fen[k];
            k &= k-1;
        }
        return ret;
    }
    int sumQueryRange(int a, int b){return sumQuery(b) - sumQuery(a - 1);}
    // changing this from MAX_SIZE to sizeof(fen) made the
    // whole difference
    void clear(){memset(fen, 0, sizeof(fen));}
    int getValue(int i){return sumQueryRange(i, i);}
    // No need of implementing findFirst so left it out
} A, B;

void changeInformation(int a, int b, int c){
    if(b < 0){
        A.adjust(a, c);
    }
    if(b == 0){
        B.adjust(a, c);
    }
}

int main(){
    int traversalArray[MAX_V];
    int a, b, c, d; // information storage
    int i, j; // for loop
    // First 2 positions _,_ to give size
    while(scanf("%d %d", &a, &b) == 2){
        // Resetting the used variables
        A.clear();
        B.clear();
        i = j = 0;
        // this actually helps print the right answer?
        char CorP[2]; // to get the C/P
        // Getting Initial Sequence
        // Start with the 1th INDEX NOT ZEROTH!!!!!!!!
        for(i = 1; i <= a; i++){
            scanf("%d", traversalArray + i); // Read into Array
            // insertion is using ONEEEE. because you're adding ONE negative OR ONE positive
            changeInformation(i, traversalArray[i], 1);
        }
        // Getting main P/C chain
        for(j = 0; j < b; j++){
            // One value in P/C
            scanf("%s %d %d", CorP, &c, &d);
            // Simplfy the program a little, & product section
            if(*CorP == 'P'){
    /*
        so we're trying to get the amount of values within that range.
        when we get the value within that range we can easily calculate
        how many values there are within this range. So when there are zero or more 0's within a product then the value of the product is zero
        SO we have 2 different fenwich trees::::
            ONE WITH zeros
            ONE WITH negative values
            if the zero is actually populated then we know that it is 0
            if there are a odd number of negative values then
            -1 * -1 * -1 = -1 not 1
            if there are a even number of negative values then
            -1 * -1 = 1..
            awesome.
    */
                if(B.sumQueryRange(c, d)){
                    // When there is one or more zeros in the interval
                    putchar('0');
                } else {
                    // When there is not zeros in the interval, and the total number of negative number between [i,j] is odd
                    // THIS IS CORRECT. -.-
                    if((A.sumQueryRange(c, d) % 2) != 0){ // holds for not 0
                        putchar('-');
                    } else {
                        // A.sumQueryRange(c, d) % 2) == 0
                        // When none of the other conditions holds then product of the interval [i,j] is positive
                        putchar('+');
                    }
                }
            } else { // Change Value == 'C'
            // Now lets try and understand the change value::
            // We are building something like a Fibbinaci sequence here when the first value is going to be zero so when we adjust the values we are able to understand that we've to use the previous value and minus it by the one we add.
            // We want to change values not product them
                // This would be to remove this value!!!!
                changeInformation(c, traversalArray[c], -1);
                // assign/change value & restructure
                traversalArray[c] = d; // set value
                // This would be to inser this value!!!
                changeInformation(c, traversalArray[c], 1);
            }
        }
        puts("");
    }
}
