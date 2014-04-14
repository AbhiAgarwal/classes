/*
- Where's Waldorf?
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#define M 50
#define N 50
#define d_size 8

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
#include <ctype.h>
using namespace std;

char grid[N*M][N*M];

// Different states:: 8
// UP DOWN LEFT RIGHT
// TOP RIGHT, TOP LEFT, BOT RIGHT, BOT LEFT
// We manipulate values for X/Y to move states
int d_x[d_size] = {-1, -1, -1, 0, 0, 1, 1, 1};
int d_y[d_size] = {-1, 0, 1, -1, 1, -1, 0, 1};

// Checks if the character is the same
bool checkSameUpperLower(char a, char b){if(tolower(a) == tolower(b)){return true;} else{return false;}}
// Check if we are IN THE BOUNDS of the grid
bool checkBounds(int currentIndex_X, int currentIndex_Y, int n, int m){return (currentIndex_X >= n || currentIndex_X < 0 || currentIndex_Y >= m || currentIndex_Y < 0);}
void findLocation(char currentWord[], int &answerOne, int &answerTwo, int n, int m){
    // First Letter
    char first = currentWord[0]; int wordSize = strlen(currentWord); bool lineIsCorrect = false;
    // Goes through n then m
    // To shorten the time we will quit whenever possible
    for(answerOne = 0; (answerOne < n); answerOne++){
        for(answerTwo = 0; (answerTwo < m); answerTwo++){
            // Now we search for the word
            // If search doesn't exist then move on
            if(!checkSameUpperLower(grid[answerOne][answerTwo], first)){continue;}
            else {
                for(int i = 0; i < d_size; ++i){
                    int currentIndex_X = answerOne, currentIndex_Y = answerTwo; lineIsCorrect = true;
                    for(int x = 0; x < wordSize; ++x){
                        // Check if we are IN BOUNDS
                        if(checkBounds(currentIndex_X, currentIndex_Y, n, m)){lineIsCorrect = false; break;}
                        // we are IN BOUNDS but maybe not same cahracter
                        // so we try all UP DOWN
                        else if(!checkSameUpperLower(grid[currentIndex_X][currentIndex_Y], currentWord[x])){lineIsCorrect = false; break;}
                        // We are MOVING UP AND DOWN AND RIGHT AND LEFT
                        else{ currentIndex_X += d_x[i]; currentIndex_Y += d_y[i]; }
                    }
                    // IF VALUES ABOVE PASS THEN MOVE ON
                    if(lineIsCorrect){return;}
                }
            }
        }
    }
}

// Parse & Call
int main(void){
    // number of the cases following
    int numberOfCases = 0;
    int answerOne = 0, answerTwo = 0, n = 0, m = 0, k = 0;
    scanf("%d", &numberOfCases);
    while(numberOfCases--){
        // Re-Initalization
        answerOne = 0, answerTwo = 0, n = 0, m = 0, k = 0;
        // pair of integers, m followed by n in decimal notation on a single line
        scanf("%d %d", &n, &m);
        // The next m lines contain n letters each
        // Quick method to take all the characters as strings and put them into the grid
        // A word can match the letters in the grid regardless of case (i.e. upper and lower case letters are to be treated as the same)
        for(int i = 0; i < n; ++i){
            scanf("%s", grid[i]);
        }
        // Following the grid of letters, another integer k appears on a line by itself
        scanf("%d", &k);
        // The next k lines of input contain the list of words to search for, one word per line
        // These words may contain upper and lower case letters only
        char currentWord[N*M];
        for(int i = 0; i < k; ++i){
            answerOne = 0, answerTwo = 0;
            scanf("%s", currentWord);
            findLocation(currentWord, answerOne, answerTwo, n, m);
            // For each word in the word list, a pair of integers representing the location of the corresponding word in the grid must be output. The integers must be separated by a single space
            // The first integer is the line in the grid where the first letter of the given word can be found (1 represents the topmost line in the grid, and m represents the bottommost line).
            // 1-index
            answerOne++;
            printf("%d ", answerOne);
            // The second integer is the column in the grid where the first letter of the given word can be found (1 represents the leftmost column in the grid, and n represents the rightmost column in the grid). If a word can be found more than once in the grid, then the location which is output should correspond to the uppermost occurence of the word (i.e. the occurence which places the first letter of the word closest to the top of the grid).
            // 1-index
            answerTwo++;
            printf("%d\n", answerTwo);
        }
        // The outputs of two consecutive cases will be separated by a blank line
        if(numberOfCases != 0){
            printf("\n");
        }
    }
}

/*
void findLocation(char currentWord[], int &answerOne, int &answerTwo, int n, int m){
    // First Letter
    char first = currentWord[0];
    bool lineIsCorrect = false;
    // printf("%c\n", firstLetter);
    // Goes through n then m
    for(answerOne = 0; answerOne < n; ++answerOne){
        for(answerTwo = 0; answerTwo < m; ++answerTwo){
            char currentElement = grid[answerOne][answerTwo];
            if(first == currentElement){
                lineIsCorrect = true;
            }
            if(lineIsCorrect){break;}
        }
        if(lineIsCorrect){return;}
    }
}

// If we have found the current index
                // We have found the first one
                int currentPosition = 0;
                lineIsCorrect = true;
                    - 8 conditions: UP, DOWN, LEFT, RIGHT, TOP LEFT, TOP RIGHT, DOWN LEFT, DOWN RIGHT


                // UP, manipulate answerOne, which is based of off n
                for(int i = 0; (i < wordSize && lineIsCorrect); ++i){
                    currentPosition = answerOne - i;
                    if(currentPosition < 0 || currentWord[i] != grid[currentPosition][answerTwo]){
                        lineIsCorrect = false;
                    }
                }

                // Down, manipulate answerOne, which is based of off n
                for(int i = 0; (i < wordSize && lineIsCorrect); ++i){
                    currentPosition = answerOne + i;
                    if(currentPosition >= n || currentWord[i] != grid[currentPosition][answerTwo]){
                        lineIsCorrect = false;
                    }
                }

                // Left, manipulate answerTwo, which is based of off m
                for(int i = 0; (i < wordSize && lineIsCorrect); ++i){
                    currentPosition = answerTwo - i;
                    if(currentPosition < 0 || currentWord[i] != grid[answerOne][currentPosition]){
                        lineIsCorrect = false;
                    }
                }

                // Right, manipulate answerTwo, which is based of off m
                for(int i = 0; (i < wordSize && lineIsCorrect); ++i){
                    currentPosition = answerTwo + i;
                    if(currentPosition >= m || currentWord[i] != grid[answerOne][currentPosition]){
                        lineIsCorrect = false;
                    }
                }

                if(lineIsCorrect){break;}

*/
