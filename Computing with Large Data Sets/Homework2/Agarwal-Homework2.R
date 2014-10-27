library(lasso2)

# ratios =  Matrix of genes - a gene is a row in this matrix.
attach("/Users/abhi/Desktop/class/Computing with Large Data Sets/Homework2/baa.ratios.rda")

# anno.trans
attach("/Users/abhi/Desktop/class/Computing with Large Data Sets/Homework2/baa.anno.trans.rda")

# anno.list
attach("/Users/abhi/Desktop/class/Computing with Large Data Sets/Homework2/baa.anno.list.rda")

# listOfGenes = list of the genes that can be predictors.
listOfGenes = as.matrix(read.table("/Users/abhi/Desktop/class/Computing with Large Data Sets/Homework2/baa.TFs.txt",
	what = "character",
	sep = "\n"
))

# Takes a character and splits it
splitCharacter <- function(currentRow) return(strsplit(currentRow, " +")[[1]])

# listPredictors = list of predictors
# matrixName = matrix with name rows
# name = name or index of the row you wish to make a regression model for
mainFunction <- function(listPredictors, matrixName, name) {
	print(name)
}

mainFunction(listOfGenes, ratios, 'GBAA0664')