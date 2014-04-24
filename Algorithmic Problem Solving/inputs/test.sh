echo "------------------------------"
echo "RUNNING UVA PROBLEM" $1
printf "OUTPUT:\n------------------------------\n"
java UVA_$1 < $1.txt
echo "------------------------------"
