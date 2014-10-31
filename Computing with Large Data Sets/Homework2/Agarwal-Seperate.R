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