/*
- Billiard Bounces
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
using namespace std;

const double PI = 3.141592653589793238463;

int main(void){
    // Considering Unlimited Tables - NO EDGES
    // The five numbers are: a > 0, b > 0, v > 0, 0 ≤ A ≤ 90, and s > 0,
    double width = 0, height = 0, velocity = 0, Angle = 0, timeEnded = 0, posX = 0, posY = 0;
    double RADIAN = (PI/180.0);
    while(scanf("%lf %lf %lf %lf %lf", &width, &height, &velocity, &Angle, &timeEnded)){
        // Reset / Maintenance
        if(width == 0 && height == 0 && velocity == 0 && Angle == 0 && timeEnded == 0){break;}
        double distanceElapsed = timeEnded * velocity;
        posX = distanceElapsed, posY = distanceElapsed;
        // PROCESS
        double currentAngle = RADIAN * Angle;
        posX *= cos(currentAngle); posY *= sin(currentAngle);
        posX /= (2.0); posY /= (2.0); posX /= width; posY /= height;
        // OUTPUT
        printf("%d %d\n", (int)round(posX), (int)round(posY));
    }
}
