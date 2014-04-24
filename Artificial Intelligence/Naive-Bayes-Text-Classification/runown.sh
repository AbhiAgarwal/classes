echo "Abhi Agarwal - Programming HW4"
echo "-------------------"
echo "Compiling Naive Bayes Text Classification"
clang++ -std=c++11 -stdlib=libc++ input.cpp
echo "-------------------"
echo "Running input file from your own corpus file"
echo "Output Naive Bayes Text Classification"
echo "-------------------"
./a.out < inputs/$1 $2 $3 $4
echo "-------------------"
