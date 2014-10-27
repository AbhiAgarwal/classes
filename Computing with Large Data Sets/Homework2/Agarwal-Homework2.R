library(lasso2)

mainDirectory <- "/Users/abhi/Desktop/class/Computing with Large Data Sets/Homework2/"
ratiosDirectory <- paste(mainDirectory, "baa.ratios.rda", sep="")
annoTransDirectory <- paste(mainDirectory, "baa.anno.trans.rda", sep="")
annoListDirectory <- paste(mainDirectory, "baa.anno.list.rda", sep="")
listOfGenesDirectory <- paste(mainDirectory, "baa.TFs.txt", sep="")

# ratios =  Matrix of genes - a gene is a row in this matrix.
attach(ratiosDirectory)

# anno.trans
attach(annoTransDirectory)

# anno.list
attach(annoListDirectory)

# listOfGenes = list of the genes that can be predictors.
listOfGenes = as.matrix(read.table(listOfGenesDirectory,
	sep = "\n"
))

# Takes a character and splits it, it splits it by space, ie: " ", but any multiple of it
splitCharacter <- function(currentRow) return(strsplit(currentRow, " +")[[1]])


# listPredictors = list of predictors
# matrixName = matrix with name rows
# name = name or index of the row you wish to make a regression model for
mainFunction <- function(listPredictors, matrixName, name) {
	print(name)
}

mainFunction(listOfGenes, ratios, 'GBAA0664')