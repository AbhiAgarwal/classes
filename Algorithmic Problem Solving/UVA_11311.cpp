/*
- Problem E
- EXCLUSIVELY EDIBLE
- Nim Game cool simulation:
    -> http://www.dst-corp.com/james/4pilnim.html
*/

#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
using namespace std;

int main(void){
    // t = the number of test cases
    int t = 0, m = 0, n = 0, r = 0, c = 0, finalAnswer = 0;
    scanf("%d", &t);
    while(t--){
        // m = width, n = length of cake
        // r/c = zero-based position of the Scrumptious Caramel Topping cake piece in the grid cake
        scanf("%d %d %d %d", &m, &n, &r, &c);
        // they know who will take the bad piece before starting
        // 4-pile Nim game
        // Nim game formulation
        // In what I'll call standard Nim, or simply Nim, the winner is the person who takes the last object
        // re-intialize
        finalAnswer = 0;

        // 1, 2, 3, then 1^2^3 == 0 makes this a losing position for the player
        // So we use this equation to play the Nim Game and get the person who loses
        // because we know that Hansel makes the first cut
        // so we can find who cuts using the Nim game formulation

        // ** HEIGHT ^ WIDTH ^ X LINE ^ Y LINE
        // then answer of gets it last gets it
        finalAnswer = c ^ r;
        // width - position(x) - 1
        finalAnswer ^= (m - r - 1);
        // height - position(y) - 1
        finalAnswer ^= (n - c - 1);

        // WHO took the last piece
        // print the name of the person that gets the bad piece
        // OR THE LAST PIECE
        if(finalAnswer == 0){
            printf("Hansel\n");
        } else {
            printf("Gretel\n");
        }
    }
}
