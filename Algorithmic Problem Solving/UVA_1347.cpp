/*
- Tour
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <algorithm>
#include <iostream>
#include <vector>
#include <cstdio>
#include <cstring>
#include <sstream>
#include <queue>
#include <math.h>
#include <limits>
using namespace std;

#define EPS 1e-9
#define PI acos(-1.0)
#define INF std::numeric_limits<int>::max()

struct point {
    double x, y;
    point() { x = y = 0.0; }
    point(double _x, double _y) : x(_x), y(_y) {}
    bool operator == (point other) const {
        return (fabs(x - other.x) < EPS && (fabs(y - other.y) < EPS));
    }
};

typedef vector<point> vecPoint;

int main(void){
    int numberOfPoints = 0; double x = 0, y = 0;
    double finalAnswer = 0.0; vecPoint allPoints;
    while(scanf("%d", &numberOfPoints) == 1){
        /* INPUT */ allPoints = vecPoint(); finalAnswer = 0.0;
        /* PARSE */
        // The point coordinates in ascending order of the x coordinate
        for(int i = 0; i < numberOfPoints; ++i){
            scanf("%lf %lf", &x, &y);
            allPoints.push_back(point(x, y));
        }
        // PROCESS
        // OUTPUT
        printf("%.2lf\n", finalAnswer);
    }
}
