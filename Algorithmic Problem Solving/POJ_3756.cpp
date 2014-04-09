/*
- Chess Game
- Time Limit:1000MS
- Memory Limit:65536KB
- 64bit IO Format:%I64d & %I64u
*/

#define MAX_N 100
#define MAX_NN 150
#define Markov_N 1500
#define STOP 2000

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
using namespace std;

int main(void){
    // Initializations
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
        instructions.clear(); instructions.assign(N + 1, 0);
        memset(cache, 0.0, sizeof(cache[0][0]) * Markov_N * (N + 1));

        /*
            - Totally there are N grids on a chessboard with numeric order
            - The players start their journeys at grid 0.
            - That there are N+1 grids totally
            - ASSUMPTIONS:
                - same position doesn't have both backwards/forwards or it wouldn't account for that
        */

        /*
            - ** the number of "go forward" instructions
            - ** get all go forward instruction
            - The first integer stands for the number of grid having the instruction
            - the second integer stand for the number of steps to go forward
        */

        scanf("%d", &Nf);
        for(int i = 0; i < Nf; ++i){
            scanf("%d", &nextPosition); scanf("%d", &nextInstruction);
            instructions[nextPosition] = nextInstruction;
        }

        /*
            - ** the number of "go backward" instructions
            - The first integer stands for the number of grid having the instruction
            - the second integer stand for the number of steps to go backward
            - We add the negative step because we're going backwards
            - The cumulative of moving forward + moving backwards
        */

        scanf("%d", &Nb);
        for(int i = 0; i < Nb; ++i){
            scanf("%d", &nextPosition); scanf("%d", &nextInstruction);
            instructions[nextPosition] = -nextInstruction;
        }

        /*
            - backwards + forwards = cumulative either
            - or -forwards + forwards = cumulative either
            - but we're NOT adding them together but assigning them
            - ** the number of "stop" instructions
            - which stand for the number of grid having the instruction
            - 2000 means that we STOP
            - N <= 100 so 2000 would work
        */

        scanf("%d", &Ns);
        for(int i = 0; i < Ns; ++i){
            scanf("%d", &nextPosition);
            instructions[nextPosition] = STOP;
        }

        /* ** PROCESSING
            - STOP DEFINITION means STOP
            - how to differeniate between STOP, BACKWARD, FORWARD
            - Markov Chain for 1000 iterations and output the expected value
        */

        /*
            - Only one instruction should be followed and there is no chain-effect for instructions
            - One can reach the goal if and only if he arrived exactly on grid N and redundant steps will change directions
            - The dice is a cube and the probability of getting every kind of points from 1 to 6 is equal to 1/6
            - There are 3 kind of instructions, go forward for 1 to 6 steps/go backward for 1 to 6 steps/no dice rolling in the next round
        */

        /*
            - probability of points from 1 to 6 is equal to 1/6
            - finalAnswer = 0.0 here
            - Multiplying the value is the same as doing divide by 6.0
        */

       double probability = (1.0/6.0);

       // WE MUST INITIALIZE SO IT CAN RUN
       // THIS WAS THE BIGGEST ERROR::
       // WAS ALWAYS IMPOSSIBLE BECAUSE OF THIS
       cache[0][0] = 1;

       // Run the Markov Chain for 1000 iterations
       // This gives us the best Expected Value E(X) for the goal
       for(int i = 0; i <= 1000; ++i){
            // Run through N grids foreach Markov we need to run each grid not just a grid
            for(int x = 0; x < N; ++x){
                // check if the probability is ever 0
                // this is if we want to ignore this case
                if(cache[i][x] > 0){
                    /*
                        - There are 3 kind of instructions::
                        - 6 different conditions/possibilities
                        - go forward for 1 to 6 steps/go backward for 1 to 6 steps/no dice rolling in the next round
                        - We need to consider from moving through ONE step to moving through to the SIXTH step so 1 <= x <= 6
                    */
                    for(int y = 1; y <= 6; ++y){
                        /*
                            Markov chain
                            - distribution for this variable depends only on the distribution of the previous state
                            - We're going through each iteration on the Chess board possible as we're going through 6 states per each N
                        */

                        int currentPosition = (x + y);

                        /*
                            - Update the Position to bring it into the correct bounds
                            - consider > N
                            - Put it back into the bounds by indexing it into
                            - the end - currentPosition
                        */

                        if(currentPosition > N){currentPosition = ((2 * N) - currentPosition);}

                        /*
                            - UPDATE NEXT STATE i + 1, and NEXT NEXT STATE with i + 2
                            - Individuals skip a state here?
                            - We are considering the SKIP example
                        */

                        if(instructions[currentPosition] == STOP){

                            /*
                                - We are to stop at this position, but to skip a state we record what we have for this current cache and this currentPosition.
                                - We're setting this value and adding it for a state that exists 2 levels from now because we're stopping for the NEXT state but recovering the state after
                            */

                            cache[i + 2][currentPosition] += (cache[i][x]*probability);
                        } else {

                            /*
                                - We are considering the NON SKIP
                                - So either backwards or forwards here
                                - We either go forwards to backwards

                                - Update Position
                                - Access the element from instructions if we should be increasing/decreasing currentPosition
                            */

                            currentPosition += instructions[currentPosition];

                            /*
                                - THE ANSWER IS WRONG WITHOUT THIS
                                - WE NEED BOTH BOUNDS
                                - because here it can be NEGATIVE or POSITIVE
                                - the value we get CAN get negative if want to move many steps BACKWARDS or when CUMULATIVE BACKWARD > CUMULATIVE FORWARD
                            */

                            if(currentPosition < 0){currentPosition = (-currentPosition);}
                            if(currentPosition > N){currentPosition = ((2 * N) - currentPosition);}
                            // Update the Element for the NEXT STATE
                            cache[i + 1][currentPosition] += (cache[i][x]*probability);
                        }
                    }
                }
            }
        }

        /* ** PROCESS THE RESULT
            - RUN MARKOV
            - To find EXPECTED VALUE we get the VALUE of the cache or the dynamic programming element and then multiply it with the ITERATION VALUE as we would for Epx(X)
        */

        for(int i = 0; i <= 1000; i++){finalAnswer += (cache[i][N] * i);}

        /*
            - if our answer is greater than 0 then we can output it
            - if it isn't then we shouldn't because that means its impossible to reach the goal state
        */

        if(finalAnswer > 0.0){answerExists = true;}

        /* ** OUTPUT
            - Representing the expectation number of the rounds to reach the goal
            - The answer should be rounded to 0.01
        */

        if(answerExists){printf("%.2lf\n", finalAnswer);}
        else { printf("Impossible\n");}
    }
}
