/*
* Block Voting
* Time Limit:3000MS
* Memory Limit:0KB
* 64bit IO Format:%lld & %llu
*/

// I'm going to be using a recursive method as this is similar to one of the problems I've done in the past. Most things will be global as I don't want to pass them through.

#include <cstdio>
#include <iostream>
#include <cstring>
using namespace std;

using namespace std;
int party = 0;
int totalNumberOfVotes, totalSumForReturn = 0; // Primary viewing
int membersOfTheParties[30], partyNumber[30]; // just 20+10 incase

void understand(int currentParty, int inputValue, int numberOfMembers){
    int currentReccomendation = totalSumForReturn + membersOfTheParties[currentParty];
    int i = 0;
    // Value that we should really be considering for our for()
    int consideration = inputValue + 1;
    // Does the dirty work where we need to increment the power index
    if((totalSumForReturn <= totalNumberOfVotes)&&(currentReccomendation > totalNumberOfVotes)){party++;}
    // If the total sum + members is greater than votes then clearly..
    // Now we have to consider the case where we have to brute force it
    // Starting from the actual indexed value so we can do like a subsequence solution
    for(i = consideration; i < numberOfMembers; i++){
        // We really just need to consider a case where we're talking about the different parties,
        // I tried the latter where I had to just consider "i", but that did not work so lets try this instead
        if(i != currentParty){
            // Pre-processing
            // We want to try and understand the sum and then recover it. We simply want to check the "ith" value but also check it with the sum currently.
            // The purpose would be just to simply check and increase the party index. We don't necessarily need it to have to keep the sum while we recurse.
            totalSumForReturn += membersOfTheParties[i];
            // Processing, during this step we need to calculate the currentParty index and see if the sum match.
            understand(currentParty, i, numberOfMembers);
            // Post Processing - Seigel method.
            totalSumForReturn -= membersOfTheParties[i];
        }
    }
}

int main(){
    // Initializing the variables
    int parties, numberOfMembers = 0;
    int partyCount = 1;
    int i, x = 0;
    scanf("%d", &parties);
    while(parties--){
        // Reset
        totalNumberOfVotes = 0;
        totalSumForReturn = 0;
        memset(membersOfTheParties, 0, sizeof(membersOfTheParties));
        memset(partyNumber, 0, sizeof(partyNumber));
        // Get the first number in the line which is number of members
        scanf("%d", &numberOfMembers);
        // Get the next number of votes within the same line
        for(i = 0; i < numberOfMembers; i++){
            partyNumber[i] = 0;
            scanf("%d", &membersOfTheParties[i]);
            totalNumberOfVotes += membersOfTheParties[i];
        }
        totalNumberOfVotes /= 2; // conversion to partyIndex
        for(x = 0; x < numberOfMembers; x++){
            totalSumForReturn = 0; // reset for calculation purposes
            party = 0;
            // Run recursive
            understand(x, -1, numberOfMembers);
            // Party should be set to zero BEFORE understand or shit will break
            printf("party %d has power index %d\n", x + 1, party);
        }
        printf("\n");
        partyCount++;
    }
    return 0;
}
