/*
- Checkers
- Time Limit:1000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
- Standard DP on a DAG. Just count the number of paths using memoization.
- 0-indexed system
*/

#define maxSize 110

#include <cstdio>
#include <algorithm>
#include <vector>
#include <iostream>
#include <cstring> // ALWAYS.
using namespace std;

char board[maxSize][maxSize];
long long cache[maxSize][maxSize];

// white piece to become a king (reach the top board row
long long dfs(int i, int x, int N){
    // If the current solution is NOT B and i IS 0 we return ONE as we have found ONE solution. This is because we need to be at the bottom of the DFS tree to be able to return back an answer to see if we've reached there or not
    // A.K.A: We've reached the top of the board AND at the top of the board we're not at B (case:: .B.B.B.. used to help), and: Target cell must be free
    // Base Case:: One Solution Found
    if(i == 0 && board[i][x] != 'B'){
        return 1;
    // Return cache if available we use memoization
    } else if(cache[i][x] != -1){
        return cache[i][x];
    // Now we're at if we're at beginning the solution where we determine branching factor & depth.
    } else {
        long long toReturn = 0;
        // From position (x;y) checker can move only forward by one of diagonals to position (x+1;y+1) or (x−1;y+1)
        // If cell (x+1;y+1) or (x−1;y+1) is occupied by enemy piece it can jump over to cell (x+2;y+2) or (x−2;y+2) respectively.
        // **
        // We will go down 2 paths. x-1/y+1 and x-2/y+2 first
        // Try x - 1; y + 1. If x -1 is greater than 0 (we don't want to reach the end), and y + 1 is less than N as we don't want to reach N (0index)
        // Going Diagnoal:
        // i - 1 by x + 1 {NEEDS UPPER BOUND}
        // i - 2 by x + 2
        // i - 1 by x - 1 {NEEDS LOWER BOUND}
        // i - 2 by x - 2
        // Ehh.. don't check board before unless you want a OUT OF BOUNDS ERROR... duh
        if((i - 1 >= 0 && x + 1 < N) && (board[i - 1][x + 1] != 'B')){
            // If the position we have moved to is NOT B. (NOT BLOCKED)
            toReturn += dfs(i - 1, x + 1, N);
        // So if -1, + 1 not possible then we can attempt to do -2, + 2 so we can jump over enimies: occupied by enemy piece it can jump over to cell
        } else if((i - 2 >= 0 && x + 2 < N) && (board[i - 2][x + 2] != 'B')){
            toReturn += dfs(i - 2, x + 2, N);
        // Now x + 1; y + 1 and x + 2; y + 2
        // We ALWAYS want to go downwards here
        // We've already tried to go upwards. We definitely don't want i going up as that would make NO sense. we want VV down.
        } if((i - 1 >= 0 && x - 1 >= 0) && (board[i - 1][x - 1] != 'B')){
                toReturn += dfs(i - 1, x - 1, N);
        // Same technique going up by noew coming down.
        } else if ((i - 2 >= 0 && x - 2 >= 0) && (board[i - 2][x - 2] != 'B')){
                toReturn += dfs(i - 2, x - 2, N);
        }
        // remember that the things we cache also should be % 1000007
        return cache[i][x] = (toReturn % 1000007L);
    }
}

int main(){
    // The number of tests T (T ≤ 100) is given on the first line
    int T = 0, currentCase = 1; scanf("%d", &T);
    while(T--){
        // Resets
        memset(board, ' ', sizeof(board[0][0]) * maxSize * maxSize);
        memset(cache, -1, sizeof(cache[0][0]) * maxSize * maxSize);
        // Next, N lines follow with N characters each describing the board itself
        int N = 0; scanf("%d", &N);
        // On the board W stands for white piece, B for black one and . for unoccupied cell
        // W index
        int Wi = 0, Wx = 0;
        // N * N Grid
        for(int i = 0; i < N; i++){
            for(int x = 0; x < N; x++){
                // There will be only one white piece in each test
                cin >> board[i][x];
                // There is only 1 W so let us save it
                // We have to DFS from the position of the white
                if(board[i][x] == 'W'){ Wi = i; Wx = x; }
            }
        }
        // As this number can be huge, output it finalAnswer modulo 1000007L.
        // Division it by long so 1000007L
        printf("Case %d: %lld\n", currentCase, (dfs(Wi, Wx, N) % 1000007L));
        currentCase++;
    }
}
