options(warn=-1)
library(lasso2)

# Setting up paths
mainDirectory <- getwd()
ratiosDirectory <- paste(mainDirectory, "/baa.ratios.rda", sep = "")
listOfGenesDirectory <- paste(mainDirectory, "/baa.TFs.txt", sep = "")

# Attaching/importing the following files into the program
attach(ratiosDirectory) # Matrix of genes - a gene is a row in this matrix.
listOfGenes = read.table(listOfGenesDirectory) # list of the genes that can be predictors.

### MAIN FUNCTION
bestModel <- function(listOfPredictors, matrix, singleRow) {
	# Pre-processing information
	matrix <- as.data.frame(matrix)
	listOfPredictors <- as.vector(as.character(listOfPredictors))

	# Filter and create a matrix, create a predictor subset
	allNames <- row.names(matrix)
	rowNames <- as.data.frame(allNames)
	matrix <- cbind(rowNames, matrix)
	predictorSubset <- matrix[matrix$allNames %in% listOfPredictors,]

	# Get the row we have to predict
	if(class(singleRow) == "character"){
		rowToPredict <- matrix[matrix$allNames == singleRow,]
	} else {
		if(singleRow == 0 || singleRow > length(matrix[,1])){
			print("Error in row number")
			return()
		} else {
			rowToPredict <- matrix[singleRow,]
		}
	}

	# Now we have everything in data.frame format: rowToPredict, predictorSubset, and matrix
	# Lasso l1ce call
	predictorSubset <- predictorSubset[,2:52]
	rowToPredict <- rowToPredict[,2:52]
	adjustedPrediction <- rbind(predictorSubset, rowToPredict)
	lastElementPosition <- length(adjustedPrediction[,1])
	adjustedPrediction <- as.data.frame(t(adjustedPrediction))

	# Find best summary using BIC
	smallestElement <- 1.79769e+308
	smallestSummary <- FALSE

	# Bounds are quite small
	for(changingBound in seq(0.01, 1.0, 0.05)){
		summary <- l1ce (
				adjustedPrediction[,lastElementPosition] ~ ., 
				data = adjustedPrediction[,1:(lastElementPosition-1)],
				sweep.out = ~1,
				x = TRUE, 
				y = TRUE,
		 		standardize = TRUE, 
		 		trace = FALSE,
		 		bound = changingBound,
		 		absolute.t = FALSE
			)

		# Calculating BIC for the current instance of l1ce
	   	RSS <- sum((adjustedPrediction[,lastElementPosition] - fitted(summary))^2)
	   	nHere <- nrow(adjustedPrediction[,1:(lastElementPosition-1)])
	   	BIC <- nHere * log(RSS / nHere) + log(nHere)

	   	# Measuring BIC against other
	   	if(!is.nan(BIC)){
	   		if(BIC < smallestElement){
	   			smallestElement <- BIC
	   			smallestSummary <- summary
	   		}
	   	}
	}

	# We have the best model now
	return(smallestSummary)
}

### MAIN FUNCTION CALL
listOfPredictors <- listOfGenes$V1
bestModel(listOfPredictors, ratios, 1020)