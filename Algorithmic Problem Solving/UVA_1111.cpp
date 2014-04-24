/*
 * Trash Removal
 * Time Limit:15000MS
 * Memory Limit:0KB
 * 64bit IO Format:%lld & %llu
*/

#include <algorithm>
#include <iostream>
#include <cstring>
#include <stdio.h>
#include <string.h>
#include <vector>
#include <cstdio>
#include <cmath>
#include <stack>
using namespace std;

#define EPS 1e-9
#define PI acos(-1.0)
#define INF 1e+37
#define NINF 1e-37
#define precision 0.05

double DEG_to_RAD(double d){
    return d * PI / 180.0;
}

double RAD_to_DEG(double r){
    return r * 180.0 / PI;
}

struct point {
    double x, y;
    point() { x = y = 0.0; }
    point(double _x, double _y) : x(_x), y(_y) {}
    bool operator == (point other) const {
        return (fabs(x - other.x) < EPS && (fabs(y - other.y) < EPS));
    }
};

struct vec {
    double x, y;
    vec(double _x, double _y) : x(_x), y(_y) {}
};

vec toVec(point a, point b){
    return vec(b.x - a.x, b.y - a.y);
}

double dist(point p1, point p2){
    return hypot(p1.x - p2.x, p1.y - p2.y);
}

double perimeter(const vector<point> &P){
    double result = 0.0;
    for (int i = 0; i < (int)P.size()-1; i++)
        result += dist(P[i], P[i+1]);
    return result;
}

double area(const vector<point> &P) {
    double result = 0.0, x1, y1, x2, y2;
    for (int i = 0; i < (int)P.size()-1; i++){
        x1 = P[i].x; x2 = P[i+1].x;
        y1 = P[i].y; y2 = P[i+1].y;
        result += (x1 * y2 - x2 * y1);
    }
    return fabs(result) / 2.0;
}

double dot(vec a, vec b){
    return (a.x * b.x + a.y * b.y);
}

double norm_sq(vec v){
    return v.x * v.x + v.y * v.y;
}

double angle(point a, point o, point b){
    vec oa = toVec(o, a), ob = toVec(o, b);
    return acos(dot(oa, ob) / sqrt(norm_sq(oa) * norm_sq(ob)));
}

double cross(vec a, vec b){
    return a.x * b.y - a.y * b.x;
}

bool ccw(point p, point q, point r){
    return cross(toVec(p, q), toVec(p, r)) > 0;
}

bool collinear(point p, point q, point r){
    return fabs(cross(toVec(p, q), toVec(p, r))) < EPS;
}

bool isConvex(const vector<point> &P){
    int sz = (int)P.size();
    if(sz <= 3) return false;
    bool isLeft = ccw(P[0], P[1], P[2]);
    for(int i = 1; i < sz-1; i++){
        if(ccw(P[i], P[i+1], P[(i+2) == sz ? 1 : i+2]) != isLeft){
            return false;
        }
    }
    return true;
}

bool inPolygon(point pt, const vector<point> &P){
    if((int)P.size() == 0) return false;
    double sum = 0;
    for(int i = 0; i < (int)P.size()-1; i++){
        if(ccw(pt, P[i], P[i+1])){
            sum += angle(P[i], pt, P[i+1]);
        } else sum -= angle(P[i], pt, P[i+1]);
    }
    return fabs(fabs(sum) - 2*PI) < EPS;
}

point lineIntersectSeg(point p, point q, point A, point B){
    double a = B.y - A.y;
    double b = A.x - B.x;
    double c = B.x * A.y - A.x * B.y;
    double u = fabs(a * p.x + b * p.y + c);
    double v = fabs(a * q.x + b * q.y + c);
    return point((p.x * v + q.x * u) / (u+v), (p.y * v + q.y * u) / (u+v));
}

vector<point> cutPolygon(point a, point b, const vector<point> &Q){
    vector<point> P;
    for(int i = 0; i < (int)Q.size(); i++){
        double left1 = cross(toVec(a, b), toVec(a, Q[i])), left2 = 0;
        if (i != (int)Q.size()-1) left2 = cross(toVec(a, b), toVec(a, Q[i+1]));
        if (left1 > -EPS) P.push_back(Q[i]);
        if (left1 * left2 < -EPS)
        P.push_back(lineIntersectSeg(Q[i], Q[i+1], a, b));
    }
    if(!P.empty() && !(P.back() == P.front())){
        P.push_back(P.front());
    }
    return P;
}

point pivot;
bool angleCmp(point a, point b){
    if(collinear(pivot, a, b)){
        return dist(pivot, a) < dist(pivot, b);
    }
    double d1x = a.x - pivot.x, d1y = a.y - pivot.y;
    double d2x = b.x - pivot.x, d2y = b.y - pivot.y;
    return (atan2(d1y, d1x) - atan2(d2y, d2x)) < 0;
}

double cross(point a, point b, point c){
    double y1 = b.y - a.y, y2 = c.y - b.y;
    double x1 = b.x-a.x, x2 = c.x-b.x;
    return x1*y2-x2*y1;
}

double height(point a, point b, point c){
    return abs(cross(a,b,c)) / dist(a,b);
}

vector<point> CH(vector<point> P){
    int i, j, n = (int)P.size();
    if(n <= 3){
        if (!(P[0] == P[n-1])) P.push_back(P[0]);
        return P;
    }
    int P0 = 0;
    for(i = 1; i < n; i++){
        if(P[i].y < P[P0].y || (P[i].y == P[P0].y && P[i].x > P[P0].x)){
            P0 = i;
        }
    }
    point temp = P[0]; P[0] = P[P0]; P[P0] = temp;
    pivot = P[0];
    sort(++P.begin(), P.end(), angleCmp);
    vector<point> S;
    S.push_back(P[n-1]); S.push_back(P[0]); S.push_back(P[1]);
    i = 2;
    while(i < n){
        j = (int)S.size()-1;
        if(ccw(S[j-1], S[j], P[i])) S.push_back(P[i++]);
        else S.pop_back();
    }
  return S;
}

int main(void){
    int caseNumber = 1, n = 0; double final; cin >> n;
    while(n > 0){
        /* PARSING */
        vector<point> allPoints; double x = 0.00, y = 0.00;
        for(int i = 0; i < n; ++i){ cin >> x >> y;
            allPoints.push_back(point(x, y));
        }
        /* PROCESSING */
        vector<point> convex = CH(allPoints); convex.push_back(convex[0]);
        final = INF;
        for(int i = 0; i < convex.size(); ++i){ double current = 0.00;
            for(int x = 0; x < n; ++x){
                double numer = abs(cross(allPoints[x], convex[i], convex[i + 1]));
                double denom = dist(convex[i], convex[i + 1]);
                current = max(current, numer / denom);
            // Sometimes it's not a number
            } if(current != 0.000000){final = min(current, final);}
        }
        /* OUTPUT */
        printf("Case %d: %.2lf\n", caseNumber, ceil(final*100)/100);
        cin >> n; caseNumber++;
    }

}
