/*
* The Monkey and the Oiled Bamboo
- i, x, y for for() loops have to be 1 cus we're counting CASES.
*/

#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

int money_size = 1000000;
int monkey[100020];

// An altered version of Binary Search, this is the other partial
// This is the harder section
int binarySearchSecond(int mid, int numberOfRungs){
    int i = 1; //eh not working
    // Weirdly when I changed it to 0 & < it worked.
    // This is because I screwed up the indexing here (ITS AN ARRAY -.-)
    for(i = 0; i < numberOfRungs; i++){
        if(monkey[i] - monkey[i - 1] > mid){ // we are too far in
            return 0; // Upper bound is correct
        }
        else if(monkey[i] - monkey[i - 1] == mid){ // we aren't too far in
            mid--; // pass by reference and not value so correct!
        }
    }
    return 1; // Upper bound is not correct = lower it
}

// Simple Binary Search Algorithm
int binarySearch(int numberOfRungs){
    // Perform Binary Search -> first section, Binary Initialization:
    int high = money_size;
    int low = 0;
    int mid;
    while(high - low > 1){
        mid = (high + low) / 2;
        if(binarySearchSecond(mid, numberOfRungs)){
            high = mid; // lower the upper bound
        } else {
            low = mid; // increase the lower bound
        }
    }
    return high;
}

int main(){
    int numberOfCases, numberOfRungs = 0;
    int i, x = 0;
    scanf("%d", &numberOfCases);
    for(i = 1; i <= numberOfCases; i++){
        memset(monkey, 0, sizeof(monkey));
        // Get Information from the current case
        scanf("%d", &numberOfRungs);
        for(x = 0; x < numberOfRungs; x++){
            scanf("%d", &monkey[x]);
        }
        int minimumValue = binarySearch(numberOfRungs);
        printf("Case %d: %d\n", i, minimumValue);
    }
    return 0;
}
