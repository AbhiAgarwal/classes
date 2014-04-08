/*
- Coconuts, Revisited
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <stdio.h>
#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
#include <iostream>
#include <cmath>
using namespace std;

/*
    Check for all iterations of people if they can get their own coconut
    and then reduce for monkey, divide for all people, check if each person can still get coconut if yes true
*/

int main(void){
    // number of coconuts gathered by a group of persons
    // (and a monkey) that were shipwrecked
    int numberOfCoconuts = 0, finalAnswerPeople = 0, finalAnswerMonkey = 1;
    int finalPersonCount = 0;
    int copynumberOfCoconuts = 0;
    bool answerFound = false;
    // ** PARSING
    while(scanf("%d", &numberOfCoconuts)){
        answerFound = false, finalAnswerPeople = 0;
        // ** EXIT CASE
        // The sequence will be followed by a negative number
        if(numberOfCoconuts == -1){break;}

        // ** PROCESSING
        // largest number of persons who could have participated in the procedure described above
        // Using brute force, solve many of the smaller cases and look for a pattern in the solution

        // Move from currentCount down to 2 (including 2)
        // We are solving for finalAnswerPeople, and we will print out finalAnswerPeople later

        // ** Thought behind this
        // Square root of the number of coconuts because that's the upper bound on how many people can potentially even split the coconut initially.
        // If there are 49 coconuts: 7 people can split it 7 ways if we assume that they all came to split it at the same time.
        // This is not the case, but the start
        finalAnswerPeople = min((int)(sqrt(numberOfCoconuts) + 1), 10);

        // O(n^2) solution for now
        // Go through i until we reach 2 people
        // We only want to go upto 2 people as we can't determine an answer with 1 person
        // We go backwards as our prediction is that the answer will be after the middle (the second half) rather than the first half
        copynumberOfCoconuts = 0;
        for(; finalAnswerPeople >= 2; finalAnswerPeople--){
            // We are going the other way now
            // This worked for smaller cases so it should for this as well
            // We duplicate numberOfCoconuts so we can alter it in each test
            copynumberOfCoconuts = numberOfCoconuts;
            for(int x = 0; x < finalAnswerPeople; x++){
                // If the number of coconuts can't be divided by the amount of people there then there aren't a fair number of coconuts and so we can't process the answer

                // We say it has to be one because it:: BECAUSE "one coconut was left over which he gave to the monkey"
                // We don't want to count the cases where the monkey can't be included

                // AH - THIS IS THE ERROR
                // USELESSLY FILTERS STUFF..

                //if((copynumberOfCoconuts % finalAnswerPeople) != 0){
                //    continue;
                //} else {

                    // If we do have one left over for the monkey then we
                    // run this algorithm
                    // We REDUCE for MONKEY and REDUCE for PEOPLE

                    // Reduce the number of coconuts by one for the monkey
                    copynumberOfCoconuts--;

                    // Divide the coconuts by the number of people + counting himself
                    // Now we are reducing it by a factor of (finalAnswerPeople + 1) and have removed the answer for the monkey
                    // Specifically check if it is
                    // We already accounted for the monkey so we can see if each person has a coconut for himself or more coconuts
                    if(copynumberOfCoconuts % finalAnswerPeople == 0){
                        // Reduce the number of coconuts by that proportion
                        // We reduce it as the person will only take his fraction of coconuts **ASSUMPTION but TRUE
                        copynumberOfCoconuts -= (copynumberOfCoconuts/finalAnswerPeople);
                    } else {
                        // If it isn't 0 then break as we don't need this particular case
                        // Each person't get his own coconut so it shouldn't work
                        break;
                    }
                }
            // Now we check each of the values to see if we have found the answer or not. If we have we clearly exit and print it out, and if we haven't found it then we keep running until we do.
            /*if((copynumberOfCoconuts % finalAnswerPeople) != 0){answerFound = false;}*/
            // We found the answer as each coconut is for each person
            // We already discounted for the monkey within the loop
            if(copynumberOfCoconuts % finalAnswerPeople == 0){answerFound = true; finalPersonCount = finalAnswerPeople; break;}
        }
        // ** OUTPUT
        // From my own tests I know that the number of monkey will always be 1 or none if answer is not found.
        // We don't need to calculate it and we keep it static in memory
        if(answerFound){
            printf("%d coconuts, %d people and %d monkey\n", numberOfCoconuts, finalPersonCount, finalAnswerMonkey);
        } else {
            printf("%d coconuts, no solution\n", numberOfCoconuts);
        }
    }
}
