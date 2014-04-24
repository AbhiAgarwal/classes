Abhi Agarwal - Programming HW4, Naive Bayes Text Classification
-------------------

All inputs are to be in the inputs/ folder.

Hi. To run my program do:
-------------------
When N <= 0, it means we're going to process half the dataset
"bash run.sh 0 0 stopwords.txt", N = Half of Dataset, DEBUG = OFF, file is stopwords.txt

To run with your own N
"bash run.sh <N> 0 stopwords.txt", replace <N> with N

Big Corpus
-------------------

"bash runbig.sh <N> 0 stopwords.txt"


Custom Run Function
-------------------
"bash runown.sh corpus.txt 5 0 stopwords.txt"

OR

"bash runown.sh <CORPUS_FILE> <N> <DEBUG> <STOPWORDS>"

<CORPUS_FILE> = corpus.txt
<N> = N
<DEBUG> = 0, 1
<STOPWORDS> = stopwords.txt

If not using bash:
-------------------
"clang++ -std=c++11 -stdlib=libc++ input.cpp"
"./a.out < inputs/corpus.txt <N> <DEBUG> <stopwords.txt>"

input your own <N>, <DEBUG>, and <stopwords.txt>

To run DEBUG mode:
-------------------
"bash run.sh 2 1 stopwords.txt", N = 2 and DEBUG = ON

COMPLETE
-------------------
bash run.sh <N> <DEBUG> <STOPFILENAME.TXT> without <...>

IE: bash run.sh 2 0 stopwords.txt

where 
N = number of entries in the corpus to use as the training set
DEBUG = 0/1 for DEBUG statements
STOPFILENAME.TXT = a filename for the stopwords.txt
