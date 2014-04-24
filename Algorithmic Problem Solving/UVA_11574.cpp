/*
- Colliding Traffic
- Time Limit:5000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
#include <cstdio>
#include <cmath>
#include <stack>
using namespace std;

#define EPS 1e-9
#define INF 1e37
#define PI acos(-1.0)
#define conversion 180.0
#define float_inefficiency 1e-2

struct point{
    double x, y, d, s; point(){ x = y = d = s = 0.0; }
    point(double _x, double _y, double _d, double _s) : x(_x), y(_y), d(_d), s(_s){} bool operator == (point other) const {
        return (fabs(x - other.x) < EPS && (fabs(y - other.y) < EPS));
    }
};

/*
point lineIntersectSeg(point p, point q, point A, point B){
  double a = B.y - A.y;
  double b = A.x - B.x;
  double c = B.x * A.y - A.x * B.y;
  double u = fabs(a * p.x + b * p.y + c);
  double v = fabs(a * q.x + b * q.y + c);
  return point((p.x * v + q.x * u) / (u+v), (p.y * v + q.y * u) / (u+v));
}
*/

// line segment p-q intersect with line A-B.
// calculate how far they are at each TIME
double distanceBetweenPoints(point A, point B, double currentTime){
    // d: NORTH -> 0, SOUTH -> 180, EAST -> 90, WEST -> 270
    // Quadratic Equation Beginning
    // Return a Calculated Solution
    // ( A xSoluton + speed * angle - B xSolution + speed * angle )^2
    // + ( A ySoluton + speed * angle - B ySolution + speed * angle )^2
    double aSin = currentTime * sin(PI * A.d / conversion),
           aCos = currentTime * cos(PI * A.d / conversion),
           bSin = currentTime * sin(PI * B.d / conversion),
           bCos = currentTime * cos(PI * B.d / conversion);
    return (pow(((A.x + A.s * aSin) - (B.x + B.s * bSin)), 2) + pow(((A.y + A.s * aCos) - (B.y + B.s * bCos)), 2));
}

// Run through all possible states
double binarySearch(point A, point B, double r, double rsquared){
    // Do a binary search through all the state spaces until we find the smallest iteration between -1000 and 1000, inclusive
    double left = 0.0, right = 10000.0, middle = 0.0;
    // Normalize L/R
    while(fabs(left - right) > float_inefficiency){
        middle = ((left + right) / 2);
        if(distanceBetweenPoints(A, B, middle) <= distanceBetweenPoints(A, B, ((middle + right) / 2))){ right = ((middle + right) / 2); }
        else{ left = ((left + right) / 2); }
    }
    // Catch the EDGE CASE after NORMALIZATION before SEARCH
    if(distanceBetweenPoints(A, B, middle) > rsquared){ return INF; }
    // ELSE if we're not in the EDGE CASE then continue BINARY SEARCH
    else { right = left; left = 0.0;
        while(fabs(left - right) > float_inefficiency){
            middle = ((left + right) / 2);
            if((distanceBetweenPoints(A, B, middle)) > rsquared){
                left = middle;
            } else { right = middle; }
        }
        return left;
    }
    return INF;
}

double dist(point p1, point p2){
    return hypot(p1.x - p2.x, p1.y - p2.y);
}

int quadratic(point p1, point p2, double &finalAnswer){
    double a;
    double b;
    double c;
}

int main(void){
    // c, the number of test cases
    // n, the number of boats -> no more than 1000 boats
    // r, the collision distance -> collide if they come within r metres of each other
    int numberOfCases = 0, n = 0, r = 0;
    // GRID: -1000 and 1000 inclusive
    // x and y give the current position of the boat as a distance east and north
    // d gives the direction in which the boat is heading in degrees clockwise from north (so east is 90 degrees)
    // s gives the speed of the boat in metres per second, and will be between 0.001 and 1000
    vector<point> xy; bool breakOut = false;
    int x_input = 0, y_input = 0, d_input = 0, s_input = 0;
    double rsquared = 0;
    double finalAnswer = 0; cin >> numberOfCases;
    while(numberOfCases--){
        x_input = 0, y_input = 0, d_input = 0, s_input = 0; xy.clear();
        cin >> n >> r; rsquared = pow(r, 2.0);
        /* Notes
            - If any two boats are within r metres of each other in their initial position, they are already collided, so you should output 0 for that case
            - The input data will be such that the answer will not change if any of the numbers x, y, d and s are changed by 10^-6 or less
        */
        /* INPUT */
        // d: NORTH -> 0, SOUTH -> 180, EAST -> 90, WEST -> 270
        for(int i = 0; i < n; ++i){
            cin >> x_input >> y_input >> d_input >> s_input;
            xy.push_back(point(x_input, y_input, d_input, s_input));
        }
        /* PROCESSING */
        // accurately solve a corresponding quadratic equation
        finalAnswer = INF;
        // FORCE SEARCH THROUGH IT
        // Each ship to any ship after that
        for(int i = 0; i < n; ++i){
            for(int x = (i + 1); x < n; ++x){
                // r, the collision distance -> collide if they come within r metres of each other
                point pointOne = xy[i], pointTwo = xy[x];
                /* EDGE CASE */
                // If any two boats are within r metres of each other in their initial position, they are already collided, so you should output 0 for that case
                if(sqrt(distanceBetweenPoints(pointOne, pointTwo, 0)) <= r + float_inefficiency){finalAnswer = 0;
                    breakOut = true;
                    break;
                } else {
                    // RUN QUADRATIC EQUATION
                    // finalAnswer = min(finalAnswer, binarySearch(pointOne, pointTwo, r, rsquared));
                    quadratic(pointOne, pointTwo, finalAnswer);
                }
            }
            if(breakOut) break;
        }
        /* OUTPUT */
        /* OUTPUT: the number of seconds, rounded to the nearest second, before any of the boats come within r metres of each other */
        if(finalAnswer < INF){
            cout << round(finalAnswer) << endl;
        } else{
            cout << "No collision." << endl;
        }
    }
}
