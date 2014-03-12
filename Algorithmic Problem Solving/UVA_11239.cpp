/*
- Open Source
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

/*
- My idea is to point each individual to a project.
- We know that there are ~ only 100 individuals in the School participating in the project so we can put them and point them to the correct project.
*/
#include <iostream>
#include <algorithm>
#include <stdio.h>
#include <map>
#include <string>
using namespace std;
// Support Functions & Variables
map<string, int> allStudentProjects;
struct Project {string name; int numberOfStudents;};
Project projectDetails[102];
bool firstUpper(const string& word){
    return word.size() && isupper(word[0]);
}
bool maxValue(Project A, Project B){
    if(A.numberOfStudents > B.numberOfStudents){
        return true;
    } else if(A.numberOfStudents < B.numberOfStudents){
        return false;
    } else if(A.name < B.name){
        return true;
    }
    return false;
}
int main(){
    string inputValue; int currentValue, projectDetailsIterator = 0;
    while(getline(cin, inputValue)){
        if(inputValue == "0"){break;}
        if(inputValue != "1"){
            // it is an offical Project as it's a capital
            if(firstUpper(inputValue)){
                projectDetails[projectDetailsIterator].name = inputValue;
                projectDetails[projectDetailsIterator].numberOfStudents = 0;
                projectDetailsIterator++;
            } else {
                // it is one of the individuals who want to join the Project
                currentValue = allStudentProjects[inputValue];
                // the value hasn't been initialized yet
                if(currentValue == 0){
                    // now we'll point the student to the project
                    // we are simply putting the individual in and saying what project he is initialized to
                    allStudentProjects[inputValue] = projectDetailsIterator;
                    projectDetails[projectDetailsIterator - 1].numberOfStudents++;
                } else if(currentValue != projectDetailsIterator && currentValue < 200){
                    // Setting an absurd random currentValue if student is already present within another project (HACK)
                    projectDetails[currentValue - 1].numberOfStudents--;
                    allStudentProjects[inputValue] = 201;
                }
            }
        } else {
            // Sorting so we can print it out by student
            sort(projectDetails, projectDetails + projectDetailsIterator, maxValue);
            int i;
            // Print out the rest
            for(i = 0; i < projectDetailsIterator; i++){
                printf("%s %d", projectDetails[i].name.c_str(), projectDetails[i].numberOfStudents);
                printf("\n");
            }
            // After printout we clear
            allStudentProjects.clear();
            projectDetailsIterator = 0;
        }
    }
    return 0;
}
