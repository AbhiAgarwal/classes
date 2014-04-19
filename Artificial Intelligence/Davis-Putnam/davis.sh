echo "Abhi Agarwal - HW3"
echo "-------------------"
echo "Compiling Davis-Putnam"
clang++ -std=c++11 -stdlib=libc++ davis_putnam.cpp
echo "-------------------"
echo "Running input file from inputs/dpin.txt"
echo "Output Davis-Putnam"
echo "-------------------"
./a.out < inputs/dpin.txt
echo "-------------------"
echo "Davis-Putnam output saved in outputs/dpout.txt"
echo "-------------------"
