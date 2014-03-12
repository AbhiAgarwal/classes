/*
- Easy Problem from Rujia Liu?
*/

#include <vector>
#include <map>
#include <iostream>
#include <cstdio>
using namespace std;

typedef map<int, vector<int> > lookup_t;
map< int, vector<int> > inputValues;

int main(){
	int n, m, k, v, i;
	while(~scanf("%d %d", &n, &m)){
		inputValues.clear();
		for(i = 0; i < n; ++i){
			scanf("%d", &k);
			lookup_t::iterator itr = inputValues.find(k);
			if(itr == inputValues.end()){
				inputValues[k] = vector<int>();
			}
			inputValues[k].push_back(i+1);
		}
		while(m--){
			scanf("%d %d", &k, &v);
			if(inputValues[v].size() < k){
				cout << 0 << endl;
			}
			else {
				cout << inputValues[v][k-1] << endl;
			}
		}
	}
	return 0;
}
