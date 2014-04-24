echo "Abhi Agarwal - Programming HW4"
echo "-------------------"
echo "Compiling Naive Bayes Text Classification"
clang++ -std=c++11 -stdlib=libc++ input.cpp
echo "-------------------"
echo "Running input file from inputs/corpus_big.txt"
echo "Output Naive Bayes Text Classification"
echo "-------------------"
./a.out < inputs/corpus_big.txt $1 $2 $3
echo "-------------------"
