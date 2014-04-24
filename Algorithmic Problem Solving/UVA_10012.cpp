/*
 * How Big Is It?
 * Time Limit:3000MS
 * Memory Limit:0KB
 * 64bit IO Format:%lld & %llu
*/

#define MAX_m 8

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
using namespace std;

vector<double> radii;

int main(void){
    /*
        - n: lines each contain a series of numbers separated by spaces
        - m: indicates how many other numbers appear on that line
        - next m: numbers on the line are the radii of the circles which must be packed in a single box
    */
    int n = 0, m = 0; double x = 0.0, finalAnswer = 0.0, width = 0.0;
    cin >> n;
    while(n--){
        /* Declerations */
        width = 0, finalAnswer = 0.000;

        /* Parsing */
        cin >> m; radii = vector<double>(m);
        for(int i = 0; i < m; ++i){cin >> x; radii.push_back(x); width += 2*x;}

        /* Processing */
        // Using Pythagorean theorem between 2 circles
        sort(radii.begin(), radii.end());
        // Code from Book
        do {

        } while (next_permutation(radii.begin(), radii.end()));

        /* Output */
        printf("%.3f\n", finalAnswer);
    }
}
