/*
- Dark roads
- Time Limit:2000MS
- Memory Limit:0KB
- 64bit IO Format:%lld & %llu
*/

#include <algorithm>
#include <cstdio>
#include <vector>
#include <queue>
using namespace std;

// UnionFind constructed from the notes in the Book
class UnionFind{
private:
  vector<int> p, rank, setSize;
  int numSets;
public:
  UnionFind(int N){
    setSize.assign(N, 1);
    numSets = N;
    rank.assign(N, 0);
    p.assign(N, 0);
    for(int i = 0; i < N; i++){
      p[i] = i;
    }
  }
  int findSet(int i){
    return (p[i] == i) ? i : (p[i] = findSet(p[i]));
  }
  bool isSameSet(int i, int j){
    return findSet(i) == findSet(j);
  }
  void unionSet(int i, int j){
    if(!isSameSet(i, j)){
      numSets--;
      int x = findSet(i);
      int y = findSet(j);
      if(rank[x] > rank[y]){
        p[y] = x; setSize[x] += setSize[y];
      } else {
        p[x] = y; setSize[y] += setSize[x];
        if(rank[x] == rank[y]){
          rank[y]++;
        }
      }
    }
  }
  int numDisjointSets(){
    return numSets;
  }
  int sizeOfSet(int i){
    return setSize[findSet(i)];
  }
};

int main(){
  int m = 0, n = 0;
  while(scanf("%d %d", &m, &n) /*&& m != 0 && n != 0*/){
    if(m == 0 && n == 0){
      break;
    }
    // Inputting values
    int x = 0, y = 0, z = 0;
    vector<pair<int, pair<int, int> > > EdgeList;
    int initialDistance = 0;
    for(int i = 0; i < n; ++i){
      // there will be a bidirectional road between x and y with length z meters
      scanf("%d %d %d", &x, &y, &z);
      EdgeList.push_back(make_pair(z, pair<int, int>(x, y)));
      initialDistance += z;
    }
    // Kruskal's Algorithm
    sort(EdgeList.begin(), EdgeList.end());
    int mst_cost = 0;
    UnionFind UF(m);
    for(int x = 0; x < n; ++x){
      pair<int, pair<int, int> > front = EdgeList[x];
      if(!UF.isSameSet(front.second.first, front.second.second)){
        mst_cost += front.first;
        UF.unionSet(front.second.first, front.second.second);
      }
    }
    printf("%d\n", initialDistance - mst_cost);
  }
}
