echo "------------------------------"
echo "RUNNING UVA PROBLEM" $1
echo "------------------------------"
echo "Compiling File"
clang++ -std=c++11 -stdlib=libc++ UVA_$1.cpp
printf "OUTPUT:\n------------------------------\n"
./a.out < inputs/$1.txt
echo "------------------------------"
