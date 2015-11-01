sbt "test"

for file in testjs/*
do
    echo "$file"
    sbt "run $file"
done