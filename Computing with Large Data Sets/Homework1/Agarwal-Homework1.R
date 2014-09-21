bg.surv <- read.csv(file = "big-data-survey-2014-fall-interests.csv")
rownames(bg.surv) <- bg.surv[,1]

# 1 -> name, 2 -> credit, 3 -> nyu, 4 -> major,
# 5 -> python, 6 -> R, 7 -> DB, 8 -> socialMedia
# 9 -> poli, 10 -> bio, 11 -> social, 12 -> civi, 13 -> evil
# 14 -> marketing, 15 -> econ, 16 -> eco
# 17 -> psyc, 18 -> activism, 19 -> history
# 20 -> pets, 21 -> entertain, 22 -> X (NA)
	
# Goes through for just you
currentPerson <- bg.surv[23, 5:21]

runHistogram <- function(usingSpearman = FALSE){

	# Initialization of variables
	closetstPerson <- -1000
	closetstPersonIndex <- 0
	furthestPerson <- 1000
	furthestPersonIndex <- 0
	similaritesList <- double()

	if(usingSpearman) 
		cat("Approach One: using spearman", "\n")
	else 
		cat("Approach One: without using spearman", "\n")

	# Goes through each individual, but me.
	for(x in 1:42){
		if(x != 23){
			# Compares using the cor function with each individual and me.
			# Either of these will use spearman or normal:
			if(usingSpearman) 
				currentCompare <- cor(unlist(currentPerson), unlist(bg.surv[x, 5:21]), method = "spearman")
			else 
				currentCompare <- cor(unlist(currentPerson), unlist(bg.surv[x, 5:21]))
			
			similaritesList <- c(similaritesList, currentCompare * 100)
			if(closetstPerson < currentCompare){
				closetstPerson = currentCompare
				closetstPersonIndex = x
			}
			if(furthestPerson > currentCompare){
				furthestPerson = currentCompare
				furthestPersonIndex = x
			}
		}
	}

	cat("Most similar to me is",  toString(bg.surv[closetstPersonIndex, 1:1]), "\n")
	cat("Most dissimilar to me is",  toString(bg.surv[furthestPersonIndex, 1:1]), "\n")

	if(usingSpearman) 
		hist(similaritesList, xlim = c(0, 100), main = "Histogram: Similarity with spearman", xlab = "Percentage Similarity")
	else 
		hist(similaritesList, xlim = c(0, 100), main = "Histogram: Similarity without spearman", xlab = "Percentage Similarity")
}

runHistogram(usingSpearman = TRUE)
cat("\n")
runHistogram(usingSpearman = FALSE)