/*
- Chess Game
- Time Limit:1000MS
- Memory Limit:65536KB
- 64bit IO Format:%I64d & %I64u
*/

#define MAX_N 100
#define MAX_NN 150
#define Markov_N 1000

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
using namespace std;

int main(void){
    // Initialization
    int N = 0, Nf = 0, Nb = 0, Ns = 0, nextPosition = 0, nextInstruction = 0;
    double finalAnswer = 0.0; // Local
    bool answerExists = false; // Local

    // Vectors
    vector<int> instructions; // Local
    // Markov Chain = 1000
    double cache[Markov_N][MAX_NN];

    // N(N ≤ 100), the number of grids on the chessboard
    // ** so that there are N+1 grids totally
    while(scanf("%d", &N) != EOF){
        // ** INIT:: Initialization for local scope
        nextPosition = 0, nextInstruction = 0, answerExists = false, Nf = 0, Nb = 0, Ns = 0, finalAnswer = 0;

        /*
        - Totally there are N grids on a chessboard with numeric order
        - The players start their journeys at grid 0.
        */

        // ** the number of "go forward" instructions
        scanf("%d", &Nf);
        // that there are N+1 grids totally
        instructions.clear(); instructions.assign(N + 1, 0);
        memset(cache, 0, sizeof(cache[0][0]) * Markov_N * (N + 1));

        // ** get all go forward instruction
        for(int i = 0; i < Nf; ++i){
            // The first integer stands for the number of grid having the instruction
            scanf("%d", &nextPosition);
            // the second integer stand for the number of steps to go forward
            scanf("%d", &nextInstruction);
            instructions[nextPosition] = nextInstruction;
        }

        // ** the number of "go backward" instructions
        scanf("%d", &Nb);
        for(int i = 0; i < Nb; ++i){
            // The first integer stands for the number of grid having the instruction
            scanf("%d", &nextPosition);
            // the second integer stand for the number of steps to go backward
            scanf("%d", &nextInstruction);
            // We add the negative step because we're going backwards
            // The cumulative of moving forward + moving backwards
            instructions[nextPosition] = -nextInstruction;
        }

        // backwards + forwards = cumulative either
        // or -forwards + forwards = cumulative either

        // ** the number of "stop" instructions
        scanf("%d", &Ns);
        for(int i = 0; i < Ns; ++i){
            // which stand for the number of grid having the instruction
            scanf("%d", &nextPosition);
            // 150 means that we STOP
            // N <= 100 so 150 would work
            instructions[nextPosition] = 150;
        }

        // ** PROCESSING
        // -1 means STOP
        // how to differeniate between STOP, BACKWARD, FORWARD
        // Markov Chain for 1000 iterations and output the expected value

        /*
            - Only one instruction should be followed and there is no chain-effect for instructions
            - One can reach the goal if and only if he arrived exactly on grid N and redundant steps will change directions
            - The dice is a cube and the probability of getting every kind of points from 1 to 6 is equal to 1/6
            - There are 3 kind of instructions, go forward for 1 to 6 steps/go backward for 1 to 6 steps/no dice rolling in the next round
        */

       // probability of points from 1 to 6 is equal to 1/6
       // finalAnswer = 0.0 here
       double probability = 1.0/6.0;

       // Run the Markov Chain for 1000 iterations
       for(int i = 1; i < 1000; ++i){
            // Run through N results
            for(int x = 0; x < N; ++x){

            }
       }

        //continue;

        // ** OUTPUT
        // representing the expectation number of the rounds to reach the goal
        // The answer should be rounded to 0.01
        if(answerExists){
            // PRINT HERE
            printf("%.2lf\n", finalAnswer);
        } else {
            printf("Impossible\n");
        }
    }
}
