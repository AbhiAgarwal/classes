#include <algorithm>
#include <sstream>
#include <iostream>
#include <fstream>
#include <vector>
#include <map>

using namespace std;

#define UNBOUND 2
#define BOUND 1
#define NOBOUND 0
#define NEGATE -1

typedef vector<vector<int>> vpairs;
typedef map<int, int> vmap;
typedef vector<bool> vbool;
typedef bool singliteral;
typedef vector<int> oneAtom;
typedef map<int, string> oneWay;
typedef map<string, int> reverseWay;

/* Simplification Functions */
int negateValue(int value){return (NEGATE * value);}

/*
  * CHECK GOAL
    - Allows us to check if we have reached the goal state
    - goal(V, S)
    - V: valuation on ATOMS satisfying S or NIL if none exists
    - sat: satisfaction
*/

bool goal(vpairs allAtoms, vbool sat, int total = 0){
  // Go through all the atoms
  for(int i = 0; i < allAtoms.size(); ++i){
    oneAtom currentAtom = allAtoms[i];
    // If it is 0 and satisfied then we increment it
    if(currentAtom.size() == 0 && sat[i] == true){total++;}
  }
  // If all of them are SATISFIED and EMPTY then return TRUE
  // else we have not because not all of them are true and not empty
  if(total == allAtoms.size()){return true;} else {return false;}
}

/*
  * CHECK EMPTY
    - isempty(A, S, V)
    - A: set of propositional atoms
    - sat: satisfaction
    - V: valuation on ATOMS satisfying S or NIL if none exists
*/

bool isempty(vpairs atoms, vbool &sat, vmap &V){
  // Go through all the atoms
  for(int i = 0; i < atoms.size(); ++i){
    // If any of them are empty then we've reached a threshold
    oneAtom currentAtom = atoms[i];
    if(currentAtom.size() == 0 && !sat[i]){ return true; }
  }
  return false;
}

/*
  * PROPAGATE
    - propagate(A, S, V)
    - L: Pure Literal Index
    - A: set of propositional atoms
    - sat: satisfaction
    - V: valuation on ATOMS satisfying S or NIL if none exists
*/

vpairs propagate(int L, vpairs atoms, vbool &sat, vmap &V){
  for(int i = 0; i < atoms.size(); ++i){
    oneAtom currentAtom = atoms[i]; int position = 0;
    /* for each clause C in S do */
    for(auto singleAtom: currentAtom){
      int absoluteSingleAtom = abs(singleAtom); int absoluteL = abs(L);
      if(absoluteSingleAtom ==  absoluteL){
        /* if ((A in C and V[A]=TRUE) or (~A in C and V[A]==FALSE)) then delete C from S */
        if((singleAtom > 0 && V[singleAtom] == BOUND) || (singleAtom < 0 && V[absoluteSingleAtom] == 0)){
            atoms[i].clear(); sat[i] = true; break;
        /* else if (A in C and V[A]==FALSE) then delete A from C */
        } else if(singleAtom > 0 && V[singleAtom] == 0){
            atoms[i].erase(atoms[i].begin() + position); sat[i] = false;
        /* else if (~A in C and V[A]==TRUE) then delete ~A from C; */
        } else if(singleAtom < 0 && V[absoluteSingleAtom] == BOUND){
            atoms[i].erase(atoms[i].begin() + position); sat[i] = false;
        }
      } position++; /* needed for .erase(...) */
    }
  }
  /* return S; */
  return atoms;
}

/*
  * Putnam Davis
    - davis(A, V, SL, S)
    - A: set of propositional atoms
    - V: valuation on ATOMS satisfying S or NIL if none exists
    - SL: Single Literal ON/OFF
    - sat: satisfaction
*/

vmap davis(vpairs atoms, vmap V, singliteral SingleLiteral, vbool sat){
  /* Check to see if we have reached the goal */
  if(goal(atoms, sat)){ return V; }
  /* Check to see if it is empty -> no result */
  else if(isempty(atoms, sat, V)){ V.clear(); return V; }
  /* Check to see if this is currently a single literal */
  else if(SingleLiteral){
    for(auto& currentV : V){
      int absoluteleCurrentV = abs(currentV.first);
      if(currentV.second && currentV.first > 0){
        if(V[(negateValue(currentV.first))]){ SingleLiteral = false; }
        else {
          currentV.second = BOUND; V[negateValue(currentV.first)] = NOBOUND;
          SingleLiteral = true;
          atoms = propagate(currentV.first, atoms, sat, V);
        }
      } else if(currentV.second && currentV.first < 0){
        if(V[absoluteleCurrentV]){ SingleLiteral = false;}
        else {
          V[absoluteleCurrentV] = NOBOUND; currentV.second = BOUND;
          SingleLiteral = true;
          atoms = propagate(currentV.first, atoms, sat, V);
        }
      }
    }
    return davis(atoms, V, SingleLiteral, sat);
  /*
    * NEITHER SINGLE OR GOAL OR EMPTY
      - Loop through the values
  */
  } else {
    for(int i = 0; i < atoms.size(); ++i){
      oneAtom currentAtom = atoms[i];
      for(auto& singleAtom: currentAtom){
        if(V[singleAtom] == UNBOUND){
          V[singleAtom] = BOUND; V[-singleAtom] = NOBOUND;
          vpairs atoms_new = propagate(singleAtom, atoms, sat, V);
          vmap V_new = davis(atoms_new, V, SingleLiteral, sat);
          if(V_new.size() != 0){ return V_new; }
          else {
            V[singleAtom] = 0; V[-singleAtom] = BOUND;
            atoms = propagate(singleAtom, atoms, sat, V);
            SingleLiteral = true;
            return davis(atoms, V, SingleLiteral, sat);
          }
        }
      }
    }
  }
  return V;
}

/*
  * Main Parsing
    - Parsing input from cin and running DP
    - main(void)
*/

int main(void){

  /*
    * INPUT
      - Take in values from standard-in
  */

  /* INPUT FOR THE STATEMENTS */

  vpairs atoms;
  string nextSentence = "";

  while(getline(cin, nextSentence)){
    oneAtom currentLine;
    istringstream currentLineStream(nextSentence);
    while(currentLineStream){
      if(!getline(currentLineStream, nextSentence, ' ')){ break; }
      if(nextSentence == "0"){ break; }
      else{ currentLine.push_back(atoi(nextSentence.c_str())); }
    }
    if(nextSentence == "0"){ break; } else { atoms.push_back(currentLine); }
  }

  /* INPUT FOR CONVERSION */

  oneWay forward;
  reverseWay backward;
  nextSentence = "";

  while(getline(cin, nextSentence)){
    istringstream currentLineStream(nextSentence);
    string inputOne = "", inputTwo = "";
    while(currentLineStream){
      if(!getline(currentLineStream, inputOne, ' ')){ break; }
      if(!getline(currentLineStream, inputTwo, ' ')){ break; }
    }
    forward[atoi(inputOne.c_str())] = inputTwo.c_str();
    backward[inputTwo.c_str()] = atoi(inputOne.c_str());
  }

  /*
    * UNBOUND
      - var V : array[ATOMS] and set UNBOUND
      - for (A in ATOMS) do V[A] := UNBOUND;
  */

  vmap V;
  for(int i = 0; i < atoms.size(); ++i){
    for(int singleAtom: atoms[i]){ V[singleAtom] = UNBOUND; }
  }

  /*
    * SAT
      - Set all SAT to FALSE
      - For then assign V[A] either TRUE or FALSE statements
  */

  vbool sat;
  sat.clear();
  for(int i = 0; i < atoms.size(); ++i){ sat.push_back(false); }

  /*
    * Run Putnam Davis Algorithm
    - returns vmap
  */

  bool Single = true;
  V = davis(atoms, V, Single, sat);

  //cout << "Putnam Davis Output" << endl;
  //cout << "-------------------" << endl;

  /*
    * Output Davis Putnam
    - Use the vmap to output the solution
  */

  ofstream writeOutputFile; // write file
  writeOutputFile.open("outputs/dpout.txt");

  if(V.size() == 0){
    /* There does not exist a solution */
    cout << "No Solution" << endl; // CONSOLE
    writeOutputFile << "No Solution" << "\n"; // FILE
  } else {
    /* There exists a solution */
    for(auto& currentV : V){
      // Meaning it's 2 so it's TRUE
      if(currentV.second == UNBOUND){ currentV.second = BOUND; }
      if(currentV.first > 0){
        cout << currentV.first << " "; // CONSOLE
        writeOutputFile << currentV.first << " "; // FILE
        if(currentV.second == BOUND){
          cout << "T" << endl; // CONSOLE
          writeOutputFile << "T" << "\n"; // FILE
        }
        else {
          cout << "F" << endl; // CONSOLE
          writeOutputFile << "F" << "\n"; // FILE
        }
      }
    }
    /* Output the Zero */
    cout << 0 << endl;
    writeOutputFile << "0" << "\n"; // FILE
    /* Output the Input Declerations */
    for(auto& currentV : forward){
      cout << currentV.first << " "; // CONSOLE
      cout << currentV.second << endl; // FILE
      writeOutputFile << currentV.first << " "; // FILE
      writeOutputFile << currentV.second << "\n"; // FILE
    }
    /* End the Output */
    //cout << "-------------------" << endl;
    writeOutputFile.close();
  }
  return 0;
}
