/*
- Prime Factors
- Time Limit:3000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
#include <bitset>
#include <cmath>
using namespace std;

void sieve(long long upperbound);
vector<int> sum(long long N);

long long _sieve_size;
bitset<10000010> bs;
vector<int> primes;

// Implementation from the book
void sieve(long long upperbound){
    _sieve_size = upperbound + 1;
    bs.set();
    bs[0] = bs[1] = 0;
    for(long long i = 2; i <= _sieve_size; i++){
        if(bs[i]){
            for(long long j = i * i; j <= _sieve_size; j += i){
                bs[j] = 0;
            }
            primes.push_back((int)i);
        }
    }
}

vector<int> sum(long long N){
    vector<int> returnProduct;
    long long PF_idx = 0, PF = primes[PF_idx], ans = 0;
    while(N != 1 && (PF * PF <= N)){
        while (N % PF == 0){
            N /= PF;
            ans += PF;
            // Didnt work well as there can exist 1 number and it's hard to test that during run-time
            /*if(firstNumber){
                printf("%lld", PF);
                firstNumber = false;
            } else {
                printf(" x %lld", PF);
            }*/
            // This will be all the values until the end
            returnProduct.push_back(PF);
        }
        PF = primes[++PF_idx];
    }
    if(N != 1){
        returnProduct.push_back(N);
    }
    return returnProduct;
}

int main(void){
    int g = 0;
    sieve(1<<16);
    while(scanf("%d", &g) != EOF){
        if(g == 0){break;}
        printf("%d = ", g);
        // If negative we just do -1 and abs it
        if(g < 0){ printf("-1 x "); g = -g; }
        // process the answer
        vector<int> answer = sum(g);
        printf("%d", answer[0]);
        for(int i = 1; i < answer.size(); i++){
            printf(" x %d", answer[i]);
        }
        printf("\n");
    }
}
