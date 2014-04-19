echo "Abhi Agarwal - HW3"
echo "-------------------"
echo "Compiling Frontend"
clang++ -std=c++11 -stdlib=libc++ frontend.cpp
echo "-------------------"
./a.out < inputs/maze.txt