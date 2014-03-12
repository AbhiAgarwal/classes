/*
- Unlock the Lock
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- BFS implementation from the book
*/

#include <stdio.h>
#include <algorithm>
#include <cstring>
#include <vector>
#include <iostream>
#include <string.h>
#include <cstdlib>
#include <string>
#include <queue>
using namespace std;

int main(){
    int currentLockCode = 0, unlockCode = 0, availableButtons = 0;
    int caseNumber = 0;
    while(scanf("%d %d %d", &currentLockCode, &unlockCode, &availableButtons) == 3){
        caseNumber++;
        // Input will be terminated when L = U = R = 0.
        if(currentLockCode == 0 && unlockCode == 0 && availableButtons == 0){
            break;
        }
        // Will run a BFS so we don't need another method
        vector<int> combinations;
        combinations.assign(availableButtons + 1, 0);
        // After that, there are R numbers (0≤ RVi ≤9999) in a line representing the value of buttons
        for(int i = 0; i < availableButtons; i++){
            scanf("%d", &combinations[i]);
        }
        // Permanently locked
        int NotPermanentlyLocked = 0, finalAnswer = 0;
        // BFS Queue Implementation from book
        vector<int> dist;
        dist.assign(11000, 2<<30);
        /*
            dist[] in this case would be the representation the number of "minimum number of button press required to unlock the lock"
            The question basically is add the values of the x buttons and see how many of them you can add to get the unlock state.
        */
        dist[currentLockCode] = 0;
        queue<int> q;
        q.push(currentLockCode);
        // Queue taken from the book
        // ch4_04_bfs
        while(!q.empty()){
            int u = q.front();
            q.pop();
            // If output == unlockCode
            // If what the value of what we're looking for is the current unlock code, then we should proceed and as we've found the answer
            if (u == unlockCode){
                //printf("%d\n", u);
                //printf("%d\n", unlockCode);
                finalAnswer = dist[u];
                NotPermanentlyLocked = 1;
                break;
            }
            // Now we loop through so we can find an unlock code
            for(int j = 0; j < availableButtons; j++){
                // u + combinations[j] will be a value higher than 10000 and we need an answer in 0000 value, so we're going to be using modulus of 10000 so solve this problem.
                int v = (u + combinations[j]) % 10000;
                // printf("v: %d u: %d comb: %d", v, u, combinations[j]);
                if(dist[v] == 2<<30){
                    dist[v] = dist[u] + 1;
                    q.push(v);
                }
            }
        }
        // Depending on the OUTPUT we have to consider "Permanently Locked"
        // or an integer
        printf("Case %d: ", caseNumber);
        if(NotPermanentlyLocked == 0){
            printf("Permanently Locked\n");
        } else {
            printf("%d\n", finalAnswer);
        }
    }
}
