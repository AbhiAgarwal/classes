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
similaritesList <- double()

for(x in 1:42){
	if(x != 23){
		currentCompare <- cor(unlist(currentPerson), unlist(bg.surv[x, 5:21]))
		similaritesList <- c(similaritesList, currentCompare*100)
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

cat("Closest person to me is",  toString(bg.surv[closetstPersonIndex, 1:1]), "\n")
cat("Furthest person to me is",  toString(bg.surv[furthestPersonIndex, 1:1]))

hist(similaritesList, xlim=c(0, 100))

# Goes through each person
# We are only comparing bg.surv[1, 5:21]
# for(i in 1:42){
	# closetstPerson <- -1000
	# closetstPersonIndex <- 0
	# furthestPerson <- 1000
	# furthestPersonIndex <- 0
	# currentPerson <- bg.surv[i, 5:21]
	# for(x in 1:42){
		# if(x != i){
			# currentCompare <- cor(unlist(currentPerson), unlist(bg.surv[x, 5:21]))
			# if(closetstPerson < currentCompare){
				# closetstPerson = currentCompare
				# closetstPersonIndex = x
			# }
			# if(furthestPerson > currentCompare){
				# furthestPerson = currentCompare
				# furthestPersonIndex = x
			# }
		# }
	# }
# }

#for(y in 1:22){
#	print(bg.surv[x, y:y])
#}