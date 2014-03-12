## Extra Functions
# Checks if featureWord function is correct
# and checks
def checkAndSet(number):
	word_set = (training_data[number].split())
	check = bool(1)
	count = 1
	while check == bool(1):
		if word_set.__len__() > count:
			feature_set = feature_list[number]
			for c, element in enumerate(feature_set):
				if feature_set[c] in vocabulary_list:
					count += 1
				elif word_set[count] not in vocabulary_list:
					print word_set[count]
					check = bool(0)
					print "Fake"
					return 0
		else:
			print check

# Finding position of a particular word in the Weight Vector
# We need to find the word in the weight vector so this would
# allow us to quickly find it and alter / get the value
# when we need too.
def findPositionInWeight(word):
	counter = 0
	for i in all_word_List:
		train = getSingleDataSet(i)
		for i in train:
			if i == word:
				return counter
		counter += 1

# Sorts words into order (if required)
# Returns Sorted List
def sortWords():
	sortedList = sorted(vocabulary_list.items(), key = lambda item: item[1])
	return sortedList

# Dot Product of Feature Vector and Weight Vector
# Takes in value, and weight and returns the dot product
# of the words
def dotProduct(values, weight):
	return dot(weight, values) # returns the dot product of them

# Creates weight set
# Creates a weight set for a particular word
# and returns that set, you've to traverse through it
# in order to get the set of all the words
def makeWeightSet(thisset):
	weight_set = [] # instance of an empty array
	singleSet = getSingleDataSet(thisset) # find the Single Set
	print singleSet
	for i in singleSet: # Go through the set of Single words
		thisVector = weight_list[i] # traverse through the word set
		weight_set.append(thisVector) # appends it to the end of the set
	return weight_set

# Copies the vocabulary list and sets it as the weight vector
# and sets each weight to 0
# Required as we need to adjust and get the weight of each word
def copyAsWeightVector():
	global weight_list # Edit the global variable
	for i in weight_list:
		weight_list[i] = 0