/*
- Battleships
- Time Limit: 1 sec
- Memory Limit: 32 MB
** Remember ships cannot touch
*/
#define MAX_N 150

#include <cstring>
#include <algorithm>
#include <iostream>
#include <string.h>
#include <stdio.h>
using namespace std;

char grid[MAX_N][MAX_N];

int searchGrid(int numberOfCharacters){
    int numberOfAliveShips = 0;
    for(int i = 0; i <= numberOfCharacters; i++){
        for (int x = 0; x <= numberOfCharacters; x++){
            // We go through each value to find x first
            // if the current position within the grid is an 'x'
            // meaning that the battle ship exists
            // Ships can be broken in the middle by @
            // so we have to account for that fact because the ship
            // can still exist if it has x@@x
            /* thought about grid[i][x] == '@' but '@.@.' case wouldn't be so great on this & it failed */
            if(grid[i][x] == 'x'){
                // We have to check if grid[i][x + 1] is an x
                // incrementer for x is required so we can move forward
                // We move landscape first ---->
                int incrementerX = (x + 1);
                // Once we have identified it as a "X" or "@" we have to reset it
                // Replace the current 'x' to '.'
                grid[i][x] = '.';
                while(grid[i][incrementerX] == 'x' || grid[i][incrementerX] == '@'){
                    // Replace all the 'x' or '@' with '.' once we see them
                    grid[i][incrementerX] = '.';
                    incrementerX++;
                }
                // We have to check if grid[i + 1][x] is an x
                // Now we move down
                int incremeneterI = (i + 1);
                while(grid[incremeneterI][x] == 'x' || grid[incremeneterI][x] == '@'){
                    grid[incremeneterI][x] = '.';
                    incremeneterI++;
                }
                numberOfAliveShips++;
            }
        }
    }
    return numberOfAliveShips;
}

int main(){
    int numberOfTests = 0;
    int numberOfCharacters = 0;
    int caseIterator = 0, printValue = 0;
    scanf("%d", &numberOfTests);
    while(numberOfTests--){
        // Initialization
        memset(grid, 0, sizeof(grid[0][0]) * MAX_N * MAX_N);
        caseIterator++;
        // Getting & Parsing Values
        scanf("%d", &numberOfCharacters);
        // numberOfCharacters * numberOfCharacters Grid Setup
        char empty;
        //scanf("%c", &empty);
        for(int i = 1; i <= numberOfCharacters; i++){
            for(int x = 1; x <= numberOfCharacters; x++){
                // cin works ALOT better than scanf here
                cin >> grid[i][x];
            }
        }
        // We have to scan through a numberOfCharacters^2 grid
        // to find all the x values that are not in a row
        // if they are in a row then they are 1 ship
        int printValue = searchGrid(numberOfCharacters);
        // Printing Values
        // caseIterator = test case number, printValue = number of still "alive" ships
        printf("Case %d: %d\n", caseIterator, printValue);
    }
    // . == stands for an empty cell
    // x == containing a ship or its part
    // @ == for already hit part of a ship
    return 0;
}
