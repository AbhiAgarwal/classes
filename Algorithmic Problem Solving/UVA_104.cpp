/*
- Arbitrage
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Slightly trickier Bellman Ford
- LETS use Floyd instead.
- Seigels book - alternative to bellman ford but using a DRIVER.
*/

// The maximum dimension is 20;
#define MAX_D 25

#include <algorithm>
#include <cstdio>
#include <vector>
#include <queue>
#include <sstream>
#include <iostream>
using namespace std;

float dimension[MAX_D][MAX_D][MAX_D];
int pathRecovery[MAX_D][MAX_D][MAX_D];

// Path Recovery is from Notes
void recoveryThatPath(int i, int j, int driver){
    // If we reach the driver, base case is 0
    if(driver == 0){
        //ss << i;
        printf("%d", i);
    // Else not
    } else {
        // Go to the next J and with a lower driver
        recoveryThatPath(i, dimension[i][j][driver], driver - 1);
        // ss << " " << j;
        printf(" %d", j);
    }
}

void fw(int N){
    int check = 0;
    for(int driver = 2; driver <= N; driver++){
        for(int k = 1; k <= N; k++){
            for(int i = 1; i <= N; i++){
                for(int j = 1; j <= N; j++){
                    // dimension[i][j] update
                    dimension[i][j][driver + 1] = min(dimension[i][j][driver + 1], dimension[i][k][driver] * dimension[k][j][1]);
                    // path recovery
                    pathRecovery[i][j][driver + 1] = k; // we are setting it as the current position, which is similar to what we do in FW
                }
            }
        }
        // run the path recovery RIGHT here.
        // DIAGONAL checks
        for(int m = 1; m <= N; m++){
            // we need 1.01 as they need to be greater than 1.0, and we are using doubles with 2 decimal places
            if(dimension[m][m][driver + 1] > 1.01){
                // Stringstream is more inefficient
                recoveryThatPath(m, m, driver + 1);
                printf("\n");
                check = -1;
            }
        }
    }
    // If no profiting sequence of n or fewer transactions exists, then the line "no arbitrage sequence exists" should be printed
    if(check == -1){
        printf("no arbitrage sequence exists\n");
    }
}

//
int main(){
    int N = 0;
    while(scanf("%d", &N) != EOF){
        memset(dimension, 0.0, sizeof(dimension));
        memset(pathRecovery, 0, sizeof(pathRecovery));
        for(int i = 1; i <= N; i++){
            for(int x = 1; x <= N; x++){
                // int driver = 1;
                double currentNumber = 0;
                if(i == x){
                    // these are assumed to have value 1.0
                    dimension[i][x][1] = 1.0;
                } else {
                    // Lets consider double..
                    scanf("%lf", &currentNumber);
                    dimension[i][x][1] = currentNumber;
                    pathRecovery[i][x][1] = i;
                }
                printf("%f\n", dimension[i][x][1]);
            }
        }
        // run FW::
        fw(N);
    }
    return 0;
}
