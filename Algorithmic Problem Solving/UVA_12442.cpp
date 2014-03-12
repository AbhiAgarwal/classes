/*
- Forwarding Emails
- Time Limit:1000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
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

// Choice of vector: We are sending emails from 1 to 2
// So we will display that as emails[1] = 2
vector<int> emails;

int findInitial(int x){

}

int main(){
    int numberOfCases = 0, currentCase = 0;
    scanf("%d", &numberOfCases);
    while(numberOfCases--){
        currentCase++;
        // Initialization
        int numberOfMartians = 0, u = 0, v = 0;
        scanf("%d", &numberOfMartians);
        // Reset Depending on the size we recieve
        emails.clear();
        emails.assign(numberOfMartians + 1, -1);
        // Parsing Information
        // 1 ≤ u, v ≤ N ::: 1 based indexing
        for(int i = 1; i <= numberOfMartians; i++){
            scanf("%d %d", &u, &v);
            emails[u] = v;
        }
        // Martian that the chieftain should send the initial email to
        int m = 0;


        printf("Case %d: %d", currentCase, m);
    }
}
