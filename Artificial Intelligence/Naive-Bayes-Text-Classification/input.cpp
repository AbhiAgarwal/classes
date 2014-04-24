/*
- The major input to the program consists of a file of short biographies (modified from Knowledge Graph and Wikipedia) of various people, tagged with a category
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
#include <cmath>
#include <sstream>
#include <fstream>
#include <map>
#include <functional>
#include <cctype>
#include <locale>

using namespace std;

#define LaplacianCorrection 0.1
#define INF 1e+37

/** SUPPORT **/

// http://stackoverflow.com/questions/236129/how-to-split-a-string-in-c
// http://stackoverflow.com/questions/216823/whats-the-best-way-to-trim-stdstring
// Split
vector<string> &split(const string &s, char delim, vector<string> &elems){stringstream ss(s); string item; while (std::getline(ss, item, delim)){elems.push_back(item);} return elems;}
vector<string> split(const string &s, char delim){vector<string> elems; split(s, delim, elems); return elems;}
// Trim
static inline std::string &ltrim(std::string &s){s.erase(s.begin(), std::find_if(s.begin(), s.end(), std::not1(std::ptr_fun<int, int>(std::isspace)))); return s;}
static inline std::string &rtrim(std::string &s){s.erase(std::find_if(s.rbegin(), s.rend(), std::not1(std::ptr_fun<int, int>(std::isspace))).base(), s.end()); return s;}
static inline std::string &trim(std::string &s){return ltrim(rtrim(s));}

/** SUPPORT ENDS **/

/*
    * PEOPLE (1)
        - First line is the name of the person
        - Second line is the category
        - The remaining lines are the biography.
*/

map<string, int> stopwords; // Words to AVOID
vector<string> trainingWords;

class People {
    public:
        // Public Methods
        string name; // ie: Bella Abzug
        string category; // ie: Government
        string biography; // ie: An American lawyer, US Representative, and social activist
        // Constructors
        People();
        People(string name, string category, string biography);
        // Public Functions foreach Person
        void toString();
        void toLower();
        void clean();
        void cleantest();
};

// Constructors
People::People(){ this->name = ""; this->category = ""; this->biography = ""; }
People::People(string name, string category, string biography){
    this->name = name; this->category = category; this->biography = biography;
}
// Convert to Lower
void People::toLower(){
    transform(this->biography.begin(), this->biography.end(), this->biography.begin(), ::tolower);
}
// Clean the training dataset
void People::clean(){
    string currentData = this->biography;
    string toUpdate = "";
    for(string singleWord: split(currentData, ' ')){
        //  any word of one or two letters WITHOUT coma, period, white, etc
        // characters in the file are all a-z, A-Z, comma, period, and white space
        singleWord.erase(std::remove(singleWord.begin(), singleWord.end(), ','), singleWord.end());
        singleWord.erase(std::remove(singleWord.begin(), singleWord.end(), '.'), singleWord.end());
        singleWord.erase(std::remove(singleWord.begin(), singleWord.end(), ' '), singleWord.end());
        if(singleWord.size() > 2){
            // REMOVE any word in the list of stopwords provided
            if(stopwords[trim(singleWord)] != 1){
                trainingWords.push_back(singleWord);
                toUpdate += singleWord + " ";
            }
        }
    }
    this->biography = toUpdate; // Update
}
// Clean the test dataset to:
//  - Additionally skip any word that did not appear at all in the training data.
void People::cleantest(){
    string currentData = this->biography;
    string toUpdate = "";
    for(string singleWord: split(currentData, ' ')){
        //  any word of one or two letters WITHOUT coma, period, white, etc
        // characters in the file are all a-z, A-Z, comma, period, and white space
        singleWord.erase(std::remove(singleWord.begin(), singleWord.end(), ','), singleWord.end());
        singleWord.erase(std::remove(singleWord.begin(), singleWord.end(), '.'), singleWord.end());
        singleWord.erase(std::remove(singleWord.begin(), singleWord.end(), ' '), singleWord.end());
        if(singleWord.size() > 2){
            // REMOVE any word in the list of stopwords provided
            if(std::find(trainingWords.begin(), trainingWords.end(), trim(singleWord)) != trainingWords.end()){
                toUpdate += singleWord + " ";
            }
        }
    }
    this->biography = toUpdate; // Update
}
// Print for DEBUG
void People::toString(){
    cout << "Name: " << this->name << endl;
    cout << "Category: " << this->category << endl;
    cout << "Biography: " << this->biography << endl << endl;
}

// Assistance with getting information
typedef vector<People> peopleVector;
typedef vector<string> stringVector;

int DEBUG = 0; // DEBUG mode ON=1/OFF=0
vector<People> allPeople; // total biography
vector<People> training; // training set
vector<People> testing; // training set
int N = 0; // number of entries in the corpus to use as the training set
int T = 0; // number of entries in the corpus to use as the testing set

// GET For each category C, the number of biographies of category C in the training set T
// <CATEGORY, SUM OF ALL PEOPLE WITH IT>
map<string, int> allCategories; // OccT(C)
// <CATEGORY, PROFILES OF ALL PEOPLE WITH IT>
map<string, peopleVector> allCategoriesPeople; // People in this category

/*
    - For each category C, the number of biographies of category C in the training set
    - For each category C, each individual who is within that particular category so we can do analysis for OccT(W|C).
*/

// Counts and classifies the training material properly for future usage
void countTraining(){
    allCategories = map<string, int>();
    allCategoriesPeople = map<string, peopleVector>();
    for(auto &eachBiography : training){
        allCategories[eachBiography.category] += 1;
        allCategoriesPeople[eachBiography.category].push_back(eachBiography);
    }
}

// Class to help us with a few sections
class wordAmount {
    public:
        string Category;
        map<string, int> howManyPresent;
        wordAmount();
};

// Constructor
wordAmount::wordAmount(){
    this->Category = "";
    this->howManyPresent = map<string, int>();
}

// Maps CATEGORY to WORD
// <CATEGORY, WORD>
typedef map<string, int> singlePersonWord;
vector<wordAmount> categoryWordPresent;

// Calculating If Each CATEGORY has a certain WORD
void countCategoryWordPresent(){
    categoryWordPresent = vector<wordAmount>();
    // Go through EACH CATEGORY
    for(auto eachCategory : allCategoriesPeople){
        wordAmount currentCategoryWord = wordAmount();
        // Set the CATEGORY to FOR THE VECTOR
        currentCategoryWord.Category = eachCategory.first;
        singlePersonWord currentIteration;
        // Go through EACH PERSON in that CATEGORY
        for(auto eachPerson : eachCategory.second){
            // Go through EACH word of PERSONS WORDS
            // Map the words so we don't do it again
            map<string, bool> markWord = map<string, bool>();
            for(string singleWord: split(eachPerson.biography, ' ')){
                // Put all words for that category into it
                // IF that word is within that CATEGORY then we know it's TRUE
                // also to add if it appears in a bibliography
                if(markWord[singleWord] == false){
                    currentIteration[singleWord] += 1;
                    markWord[singleWord] = true;
                }
            }
        }

        for(auto eachWord : trainingWords){
            if(!currentIteration[eachWord]){
                currentIteration[eachWord] = 0;
            }
        }

        // Push it into the general category for future uses.
        currentCategoryWord.howManyPresent = currentIteration;
        categoryWordPresent.push_back(currentCategoryWord);
    }
}

// GET OccT(W)
int getOccT(string C){
    return allCategories[C];
}

// OccT (W|C) TO CHECK IF WORD EXISTS IN THAT CATEGORY
// the number of biographies of category C that contain word
int getOccT(string word, string category){
    for(auto eachCategory : categoryWordPresent){
        if(eachCategory.Category == category){
            // Returns how many bibliographys have OccT(W|C)
            return eachCategory.howManyPresent[word];
        }
    }
    return 0;
}

/* - Easy for debugging reasons */

// Print the OccT(C) values
void countOccTCtoString(){
    for(auto eachCategory : allCategories){
        printf("%s: %d\n", eachCategory.first.c_str(), eachCategory.second);
    }
}

// Print the OccT(W|C) values
void countOccTWCToString(){
    for(auto eachCategory : categoryWordPresent){
        printf("Current Category %s\n", eachCategory.Category.c_str());
        for(auto eachWord : eachCategory.howManyPresent){
            printf("Word: %s, Frequency: %d\n", eachWord.first.c_str(), eachWord.second);
        }
        printf("\n");
    }
}

/* * CLASSIFICATION */

// Class to increase our flexibility
class freqwc {
    public:
        string Category;
        map<string, double> howManyPresent;
        freqwc();
};

// Constructor
freqwc::freqwc(){
    this->Category = "";
    this->howManyPresent = map<string, double>();
}

// Class to increase our flexibility
class personcb {
    public:
        People currentPerson;
        map<string, double> eachCategory;
        personcb();
};

// Constructor
personcb::personcb(){
    this->currentPerson = People();
    this->eachCategory = map<string, double>();
}

// Class to increase our flexibility
class predictioClassifier {
    public:
        People currentPerson;
        string predictionValue;
        map<string, double> categoryValue;
        map<string, double> xi;
        // Let m = mini ci, the smallest value of these
        double m;
        predictioClassifier();
};

// Constructor
predictioClassifier::predictioClassifier(){
    this->currentPerson = People();
    this->predictionValue = "";
    this->m = 0.0;
}

// Class to help us do classification
class classifythis {
    public:
        map<string, double> frequencyC;
        vector<freqwc> frequencyWC;
        map<string, double> probabilityC;
        vector<freqwc> probabilityWC;
        map<string, double> logC;
        vector<freqwc> logWC;
        vector<personcb> testBiography;
        vector<predictioClassifier> predictionMade;

        classifythis();
        void frequencyCCalculate();
        double getFreqC(string category);
        void frequencyWCCalculate();
        double getFreqWC(string word, string category);
        void probabilityCCalculate();
        void probabilityWCCalculate();
        void logCCalculate();
        void logWCCalculate();
        void LCB();
        void findPrediction();
};

// Constructor to initialize the value
classifythis::classifythis(){
    this->frequencyC = map<string, double>();
    this->frequencyWC = vector<freqwc>();
    this->probabilityC = map<string, double>();
    this->probabilityWC = vector<freqwc>();
    this->logC = map<string, double>();
    this->logWC = vector<freqwc>();
    this->predictionMade = vector<predictioClassifier>();
}

// Runs through each case to find the Freq(C)
// the fraction of the biographies that are of category C.
void classifythis::frequencyCCalculate(){
    for(auto eachCategory : allCategoriesPeople){
        frequencyC[eachCategory.first] = (double)((double)eachCategory.second.size()/(double)training.size());
    }
}

// Get any Freq(C) value using this function
double classifythis::getFreqC(string category){
    return this->frequencyC[category];
}

// Calculates FreqT(W|C)
// the fraction of biographies of category C that contain W
void classifythis::frequencyWCCalculate(){
    // Go through EACH category
    for(auto eachCategory : categoryWordPresent){
        freqwc currentCategoryWC;
        currentCategoryWC.Category = eachCategory.Category;
        // How many people in a category
        int categorySize = allCategoriesPeople[currentCategoryWC.Category].size();
        // Go through EACH word in the category
        map<string, double> currentInput = map<string, double>();
        for(auto eachWord : eachCategory.howManyPresent){
            currentInput[eachWord.first] = (double)((double)eachWord.second)/((double)categorySize);
        }
        currentCategoryWC.howManyPresent = currentInput;
        frequencyWC.push_back(currentCategoryWC);
    }
}

// Get any Frequency(W|C) value using this function
double classifythis::getFreqWC(string word, string category){
    for(auto eachFrequency : this->frequencyWC){
        if(category == eachFrequency.Category){
            for(auto eachWord : eachFrequency.howManyPresent){
                if(eachWord.first == word){
                    return eachWord.second;
                }
            }
        }
    }
    return 0;
}

// Calculates P(C)
// For each classification C and word W, compute the probabilities using the Laplacian correction (LaplacianCorrection GLOBAL)
void classifythis::probabilityCCalculate(){
    int categorySize = this->frequencyC.size();
    for(auto eachCategory : this->frequencyC){
        this->probabilityC[eachCategory.first] = ((double)(eachCategory.second + LaplacianCorrection))/((double)(1 + (categorySize * LaplacianCorrection)));
    }
}

void classifythis::probabilityWCCalculate(){
    // Go Through EACH category
    for(auto eachCategory : this->frequencyWC){
        freqwc currentCategoryWC;
        currentCategoryWC.Category = eachCategory.Category;
        map<string, double> currentCategory = map<string, double>();
        for(auto eachWord : eachCategory.howManyPresent){
            currentCategory[eachWord.first] = (double)((double)(eachWord.second + LaplacianCorrection))/((double)(1 + 2 * LaplacianCorrection));
        }
        currentCategoryWC.howManyPresent = currentCategory;
        this->probabilityWC.push_back(currentCategoryWC);
    }
}

// Calculate the Log(C) value
void classifythis::logCCalculate(){
    for(auto currentC : this->probabilityC){
        this->logC[currentC.first] = -log2(currentC.second);
    }
}

// Calculate the Log(W|C) value
void classifythis::logWCCalculate(){
    for(auto eachCategory : this->probabilityWC){
        freqwc currentCategoryWC;
        currentCategoryWC.Category = eachCategory.Category;
        map<string, double> currentCategory = map<string, double>();
        for(auto eachWord : eachCategory.howManyPresent){
            currentCategory[eachWord.first] = -log2(eachWord.second);
        }
        currentCategoryWC.howManyPresent = currentCategory;
        this->logWC.push_back(currentCategoryWC);
    }
}

void classifythis::LCB(){
    // Go through each TEST PERSON
    for(auto eachPerson : testing){
        // Initialize a new PERSON
        personcb newPerson = personcb();
        newPerson.currentPerson = eachPerson;
        map<string, double> eachNewCategory = map<string, double>();
        // GO THROUGH each CATEGORY regardless of TEST CATEGORY
        for(auto eachCategory : allCategories){
            // L(C|B) += Log(Category)
            eachNewCategory[eachCategory.first] += this->logC[eachCategory.first];
            // L(C|B) += Log(Word|Category)
            // Go through EACH word
            map<string, bool> wordAppears = map<string, bool>();
            for(string eachWord: split(eachPerson.biography, ' ')){
                if(wordAppears[eachWord] != true){
                    for(auto currentLWC : logWC){
                        if(currentLWC.Category == eachCategory.first){
                            eachNewCategory[eachCategory.first] += currentLWC.howManyPresent[eachWord];
                            wordAppears[eachWord] = true;
                        }
                    }
                }
            }
        }
        newPerson.eachCategory = eachNewCategory;
        this->testBiography.push_back(newPerson);
    }
}

void classifythis::findPrediction(){
    for(auto eachTestSubject : this->testBiography){
        predictioClassifier eachPerson;
        string currentPrediction = "";
        double currentPredictionValue = INF;
        for(auto analysis : eachTestSubject.eachCategory){
            eachPerson.categoryValue[analysis.first] = analysis.second;
            if(analysis.second < currentPredictionValue){
                currentPredictionValue = analysis.second;
                currentPrediction = analysis.first;
            }
        }
        eachPerson.currentPerson = eachTestSubject.currentPerson;
        eachPerson.predictionValue = currentPrediction;
        eachPerson.m = currentPredictionValue;
        this->predictionMade.push_back(eachPerson);
    }
    for(auto &eachPrediction : this->predictionMade){
        // s = SUM{i}[xi],
        double s = 0.0;
        // Step1. L(Ci|B) the values of L for all the different categories
        for(auto &eachCategory : eachPrediction.categoryValue){
            if(eachCategory.second - eachPrediction.m < 7){
                // xi value
                eachPrediction.xi[eachCategory.first] = pow(2, (eachPrediction.m - eachCategory.second));
            } else {
                eachPrediction.xi[eachCategory.first] = 0;
            }
            s += eachPrediction.xi[eachCategory.first];
        }
        // Step2. Now For i = 1 ... k, P(Ck|B) = xi/s.
        for(auto &eachCategory : eachPrediction.xi){
            eachCategory.second = (double)((double)(eachCategory.second)/(double)s);
        }
    }
}

/* DEBUG -> Prints out all information */
void debugmode(){
    for(auto eachPerson: allPeople){eachPerson.toString();}
    printf("N: %d\n", N);
    countTraining(); countOccTCtoString();
    countOccTWCToString();
}

/* Main -> Parsing, Debugging */
int main(int argc, char** argv){

    /** DEBUG MODE **/ if(argv[2]){if(atoi(argv[2])==1){DEBUG = 1;}}

    /** (2) PARSING STARTS **/

    /* 1. GET INFORMATION FROM "corpus.txt" */
    string nextSentence = ""; int currentPosition = 0;
    People currentPerson;
    while(getline(cin, nextSentence)){
        if(nextSentence.empty()){
            if(currentPerson.name != ""){
                allPeople.push_back(currentPerson);
                currentPerson = People(); currentPosition = 0;
            }
        } else {
            if(currentPosition == 0){
                currentPerson.name = nextSentence; currentPosition++;
            } else if(currentPosition == 1){
                currentPerson.category = nextSentence; currentPosition++;
            } else if(currentPosition == 2){
                currentPerson.biography.append(nextSentence + " ");
            }
        }
    }
    // Push in the person that was left out because of EOF
    // and check to see if blank lines to error it up
    if(currentPerson.name != ""){
        allPeople.push_back(currentPerson);
    }

    /** 2. GET INFORMATION FOR "user input of an integer N" **/
    // NO INPUT -> SET TO HALF
    if(argv[1]){
        if(atoi(argv[1]) <= 0){ N = (allPeople.size()/2); }
        else { N = atoi(argv[1]); }
    } else { N = (allPeople.size()/2); }
    // ALSO SET T:: SET FOR TESTS
    T = (allPeople.size() - N);

    // CHECK IF N > SIZE
    if(N > allPeople.size()){
        printf("N is bigger than corpus.txt\n");
        return 0;
    } else if(N == allPeople.size()){
        printf("Do you really want all the data to be training set?\n");
        printf("Oh Well!\n");
        printf("-------------------\n");
    }

    string inputStopWords = "";
    if(argv[3]){ inputStopWords = argv[3]; } // ALTER IF NEEDED
    else{inputStopWords = "stopwords.txt";}
    string stopWordsFile = string("inputs/") + inputStopWords;

    /* 3. GET INFORMATION FROM "stopwords.txt" */
    ifstream stopline(stopWordsFile.c_str()); string line = "";
    if(stopline.is_open()){
        while(getline(stopline, line)){
            for(string singleWord: split(line, ' ')){
                stopwords[trim(singleWord)] = 1;
            }
        }
        stopline.close();
    } else {
        cout << "INPUT STOPWORDS -> FILE NOT FOUND" << endl;
        cout << "READ README.md" << endl;
    }

    /** PARSING ENDS **/

    /** LEARNING STARTS **/

    /** (3.1.1) NORMALIZATION STARTS **/

    // 1. SEPERATE TRAINING/TESTING DATA
    for(int i = 0; i < N; ++i){
        training.push_back(People(allPeople[i].name, allPeople[i].category, allPeople[i].biography));
    }
    for(int i = N; i < allPeople.size(); ++i){
        testing.push_back(People(allPeople[i].name, allPeople[i].category, allPeople[i].biography));
    }

    // 2. All words in the biography should be normalized to lower case
    // 3. Stop words should be omitted.
    //  - any word of one or two letters
    //  - any word in the list of stopwords provided
    // ** MUST BE A REFERENCE SO IT CAN UPDATE GLOBALLY!
    for(auto &eachPerson : training){eachPerson.toLower(); eachPerson.clean();}

    /** NORMALIZATION ENDS **/

    /** (3.1.2) COUNTING STARTS **/

    // * For each category C, the number of biographies of category C in the training set T, OccT (C)
    countTraining(); // OccT(C).
    // * Process Bernoulli method
    countCategoryWordPresent(); // OccT (W|C)

    cout << "Parsing Completed Successfully!" << endl;
    cout << "-------------------" << endl;

    /** COUNTING ENDS **/

    /** PROBABILITIES STARTS **/

    classifythis C;

    /* STEP 1 */

    // define FreqT(C) = OccT(C)/|T|
    // the fraction of the biographies that are of category C
    C.frequencyCCalculate();
    // define FreqT(W|C) = OccT (W|C)/OccT (C)
    // the fraction of the biographies that are of category C
    C.frequencyWCCalculate();

    /* STEP 2 */

    // P(C) for each CATEGORY
    C.probabilityCCalculate();
    // P(W|C) foreach WORD and CATEGORY
    C.probabilityWCCalculate();

    // Compute negative log probabilities to avoid underflow
    // L(C) value calculation
    C.logCCalculate();

    // L(W|C) value calculation
    C.logWCCalculate();

    /** PROBABILITIES ENDS **/

    /** LEARNING OVER **/

    cout << "Learning Completed Successfully!" << endl;
    cout << "-------------------" << endl;

    /** APPLYING STARTS **/
    // ONLY USE THE ONES IN THE TEST DATA NOW

    // Applying the classifier to the test data
    // ** Normalize the text as in part I. Additionally skip any word that did not appear at all in the training data.
    // Seperate cleantest necessary so we can normalize and skip words that weren't in.
    for(auto &eachPerson : testing){
        eachPerson.toLower(); eachPerson.cleantest();
    }

    // For each category C, compute L(C|B)
    // L(C|B) = L(C) + SUM {W subset B} [L(W|C)]
    C.LCB();

    // The prediction of the algorithm is the category C with the smallest value of L(C|B). It is extremely unlikely that you will ever get a tie; if you do, it may be broken arbitrarily
    // Do Classification for Test
    C.findPrediction();

    // The prediction of the algorithm is the category C with the smallest value of L(C|B).

    /** APPLYING STOPS **/
    cout << "Applying Completed Successfully!" << endl;
    cout << "-------------------" << endl;

    cout << "Output: Overall accuracy" << endl;
    cout << "-------------------" << endl;

    // Print the output
    int numberRight = 0;
    // Go through EACH prediction
    for(auto eachPrediction : C.predictionMade){
        // Print Person Name, etc.
        printf("%s. Prediction: %s. ", eachPrediction.currentPerson.name.c_str(), eachPrediction.predictionValue.c_str());
        // Print RIGHT or WRONG
        if(eachPrediction.predictionValue == eachPrediction.currentPerson.category){ printf("Right.\n"); numberRight++;}
        else { printf("Wrong.\n"); }
        // Print each category values
        for(auto currentCategory : eachPrediction.xi){
            printf("%s: %.2lf \t", currentCategory.first.c_str(), currentCategory.second);
        }
        printf("\n\n");
    }
    printf("Overall accuracy: %d out of %d = %.2lf.\n", (int)numberRight, (int)C.predictionMade.size(), (double)(((double)numberRight)/((double)C.predictionMade.size())));

    /** DEBUG STARTS **/

    if(DEBUG == 1){debugmode();}

    /** DEBUG ENDS **/

    // FUNCTION ENDS
    return 0;
}
