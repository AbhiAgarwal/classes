library(lasso2)

# Setting up paths
mainDirectory <- "/Users/abhi/Desktop/class/Computing with Large Data Sets/Homework2/"
ratiosDirectory <- paste(mainDirectory, "baa.ratios.rda", sep = "")
listOfGenesDirectory <- paste(mainDirectory, "baa.TFs.txt", sep = "")

# Attaching/importing the following files into the program
attach(ratiosDirectory) # Matrix of genes - a gene is a row in this matrix.
listOfGenes = as.matrix(read.table(listOfGenesDirectory, sep = "\n")) # list of the genes that can be predictors.

# Not useful, but will keep in this project
# annoTransDirectory <- paste(mainDirectory, "baa.anno.trans.rda", sep="")
# annoListDirectory <- paste(mainDirectory, "baa.anno.list.rda", sep="")
# attach(annoTransDirectory) # anno.trans
# attach(annoListDirectory) # anno.list

### HELPER FUNCTIONS
# Takes a character and splits it, it splits it by space, ie: " ", but any multiple of it
splitCharacter <- function(currentRow) return(strsplit(currentRow, " +")[[1]])

# Takes a matrix and splits it to give only words, for example: "GBAA5585  is TF" to "GBAA5585"
splitListIntoCharacters <- function(listPredictors){
	onlyNames <- list()
	for(i in 1:length(listPredictors)){
		onlyNames <- c(onlyNames, splitCharacter(listPredictors[i])[1])
	}
	return(onlyNames)
}

### MAIN FUNCTION
mainFunction <- function(listOfPredictors, allRowNames, nameOfRow) {
	print(allRowNames)
}


### MAIN FUNCTION CALL
listOfPredictors <- splitListIntoCharacters(listOfGenes)
matrixWithNamesRows <- row.names(ratios)
mainFunction(listOfPredictors, matrixWithNamesRows, 'GBAA0664')