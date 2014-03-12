'''
The Data has been removed:
Against class policy
If you'd like to see the data with the work - 
contact me (Twitter @AbhiAgarwal)
'''

import math, time, sys
all_data = [] # Training + Validation Data
training_data = [] # Training Data
validation_data = [] # Validation Data
test_data = [] # Test Data
weight_list = {} # Weight List
feature_list = [] # Feature Set
vocabulary_list = {} # vocabulary/word list
train_vector_list = [] # Vector for Training
validate_vector_list = [] # Vector for Validation
weights_pegasos = [] # THIS ONE WE USE FOR PEGASOS WEIGHT 
index_vocab = []

# @Gets data from train & test
# @Returns nothing
def getData():
	global training_data, validation_data, test_data # Editing the global varabile
	print "		-> Getting Test, Validation & Training Data from {DATA SOURCE}"
	startTime = time.time() # Starts Timer
	train = open('', 'r') # Declaration of files
	test = open('', 'r') # Declaration of files
	for index, data in enumerate(train): # Getting data from the files
		if data:
			all_data.append(data)
			if index < 4000: # Getting 0-3999 data to training set
				training_data.append(data)
			else: # Getting 4000-5000 data to validation set
				validation_data.append(data)
	for testdata in test: # Getting test data from the files
		if testdata: # Getting 0-1000 data to test set
			test_data.append(testdata) # put data into test_data array
	print "			-> Took " + str(time.time() - startTime) + " seconds"
	print "			-> There are " + str(len(all_data)) + " words in all the Data Set"
	print "			-> There are " + str(len(training_data)) + " words in the Training Data Set"
	print "			-> There are " + str(len(validation_data)) + " words in the Validation Data Set"
	print "			-> There are " + str(len(test_data)) + " words in the Test Data Set"
	return

# @Splits data into many words, and removes '0' or '1'
# @Returns Split & Removed of '0', '1' Data Set
def getSingleDataSet(data):
	if "-all" in sys.argv:
		print "Calling getSingleDataSet"
	data_set = data.split() # splits the giant array/dict
	for index, data in enumerate(data_set): # goes through the data set 
		if data == '1': # deletes 1, cleanup
			del data_set[index]
		elif data == '0': # deletes 0, cleanup
			del data_set[index]
	return data_set

# @Gets list of words which occur 30 or more times
# @Returns nothing
def rankWords(data, dataset):
	global vocabulary_list, weight_list
	print "		-> Putting words into Vocabulary & Weight List"
	print "		-> Dataset: " + dataset
	startTime = time.time() # Starts Timer
	vocabulary_list_before = {} # before you do < 30 choosings
	for i in range(0, len(data)): # goes through the first 4000 words
		word_set = getSingleDataSet(data[i]) # splits
		for word in word_set:
			if word in vocabulary_list_before:
				vocabulary_list_before[word] += 1 # adds if the word is there
			else:
				vocabulary_list_before[word] = 1 # if it hasn't been created
		weight_list = vocabulary_list_before # set one instance of the vocabulary list as the weight
	print "		-> Removing words occuring less than 30 times"
	for i in vocabulary_list_before: # goes through all words above 30 appearances or more
		if vocabulary_list_before[i] >= 30: # puts values of above 30 into the vocabulary list
			vocabulary_list[i] = vocabulary_list_before[i]
	index_vocab = vocabulary_list.keys()
	for i in weight_list:
		weight_list[i] = 0 # Initilizes it all to zero
	print "			-> Took " + str(time.time() - startTime) + " seconds"
	print "			-> There are " + str(len(vocabulary_list)) + " words in the Vocabulary List"
	return

# You've to go through all the vocab and tick 1 or 0 if its there
# @Returns nothing
def featureWord(email_list, dataset):
	global feature_list # Edit the global varabile
	index_vocab = vocabulary_list.keys()
	print "		-> Creating Feature Set"
	print "		-> Dataset: " + dataset
	startTime = time.time() # Starts Timer
	counter = 0
	while (counter < len(email_list)):
		word_set = getSingleDataSet(email_list[counter])
		for i, word in enumerate(index_vocab):
			if(word in word_set):
				keys = index_vocab.index(word)
				feature_list[counter][keys] = 1
		counter += 1
		print counter
	print "			-> Took " + str(time.time() - startTime) + " seconds"
	print "			-> There are " + str(len(feature_list)) + " words in the feature list, and " + str(len(feature_list[1])) + " elements in each."
	return

# @Determines if it is spam or not
# @Returns -1 or 1
def spamornot(word_list, number):
	if "-all" in sys.argv:
		print "Calling spamornot"
	toBeDetermined = word_list[number] # checks the word to be determined
	if toBeDetermined[:2].replace(" ", "") == "1": # checks if the first "x " is 1 or 0
		return 1 # returns 1 if spam
	elif toBeDetermined[:2].replace(" ", "") == "0":
		return 0 # returns 0 if not spam

# Splits vector training, and validation into 4000:1000 split
def createVector():
	if "-all" in sys.argv:
		print "Calling createVector"
	global validate_vector_list, train_vector_list
	train_vector_list = feature_list[0:4000]
	validate_vector_list = feature_list[4000:5000]

def vectorAdd(vecOne, vecTwo):
	if "-all" in sys.argv:
		print "Calling vectorAdd"
	vec = []
	for one, two in zip(vecOne, vecTwo):
		vec.append(one + two)
	return vec

def vectorMult(scalarQ, vecAr):
	if "-all" in sys.argv:
		print "Calling vectorMult"
	for index, vector in enumerate(vecAr):
		vecAr[index] *= scalarQ
	return vecAr

def dotFunction(vecOne, vecTwo):
	if "-all" in sys.argv:
		print "Calling dotFunction"
	return sum(one * two for one, two in zip(vecOne, vecTwo))

def vectorMagnitude(vector):
	if "-all" in sys.argv:
		print "Calling vectorMagnitude"
	div = float(0)
	for values in vector:
		div += float(math.pow(values, 2))
	magnitude = float(math.sqrt(div))
	return 0.001 if (div == 0.0) else magnitude

def evaluateFunc(weight, lambd, two):
	if "-all" in sys.argv:
		print "Calling evaluateFunc"
	obj = ((lambd / 2) * math.pow(vectorMagnitude(weight), 2)) + two
	return obj

def hingeloss(x, y):
	return (float(1) / (float(x))) * y

# @Trains using Support Vector Machine Algorithm
# @Returns final classification vector
def pegasos_svm_train(vector, data, lambd, iteration):
	global weights_pegasos
	print "		-> Training using the Pegasos Algorithm"
	startTime = time.time() # Starts Timer
	obj_avg = 0
	hinge_avg = 0
	hing_avg_answer = 0
	iterations = 1 # Num of passes through data
	weights_pegasos = [0 for i in range(0, len(vector))] # Initilize the Weight
	u = [0 for i in range(0, len(vector))] # Initlize the "u"
	t = 0 # Element value
	nt = 0 # n subscript t value
	for i in range(0, iteration): # 20 passes through data
		overone = 0 # > 1
		underone = 0 # < 1
		evalution = 0 # eval
		for index, vec in enumerate(vector): # Traversal through the Feature List
			t += 1
			nt = ((float(1))/(t * lambd))
			yj = 1 if (spamornot(data, index) == 0) else -1
			if((yj * dotFunction(weights_pegasos, vec)) < 1):
				u = vectorAdd(vectorMult((1 - (nt * lambd)), weights_pegasos), vectorMult((nt * yj), vec))
				underone += 1
			else:
				u = vectorMult((1 - (nt * lambd)), weights_pegasos)
				overone += 1
			val = min(1, ((1 / (math.sqrt(lambd))) / (vectorMagnitude(u))))
			tempo = vectorMult(val, u)
			weights_pegasos = tempo[:]
			evalution += max(0, (1 - (nt * dotFunction(weights_pegasos, vec))))
			del u[:] #reset
		obj_val = evaluateFunc(weights_pegasos, lambd, evalution)
		obj_avg += obj_val
		hinge_avg = hingeloss(len(vec), evalution)
		# Obj_val is to plot (f(wt))
		print "			-> Hinge Loss for iteration number " + str(iterations) + " is: " + str(hinge_avg)
		print "			-> Lower Than One: " + str(underone) + " Over One: " + str(overone)
		print "			-> Number: " + str(iterations) + " Value: " + str(obj_val)
		hing_avg_answer += hinge_avg
		iterations += 1
	print "			-> Average Hinge Loss: " + str((hing_avg_answer)/(iteration))
	print "			-> Took " + str(time.time() - startTime) + " seconds"
	return weights_pegasos

def checkValue(vector, weight):
	if "-all" in sys.argv:
		print "Calling checkValue"
	result = 1 if (dotFunction(vector, weight) > 0) else -1
	return result

# @Tests the SVM Algorithm
# @Returns nothing
def pegasos_svm_test(weight, feature, data):
	print "		-> Starting Test"
	print "		-> Dataset: " + "Test Data, using " + str(len(feature))
	startTime = time.time() # Starts Timer
	errors = 0
	for index, vec in enumerate(feature):
		yj = 1 if (spamornot(data, index) == 0) else -1
		result = checkValue(vec, weight)
		if result == yj:
			continue
		else:
			errors += 1
	notRight = (float(errors) / (float(len(feature))))
	print "			-> Error: " + str(errors) + ", out of: " + str(len(feature)) + ". Has " + str(notRight) + "% errors"
	print "			-> Took " + str(time.time() - startTime) + " seconds"	
	return

# @Main function
# @Returns nothing
if __name__ == '__main__':
	if "-run" in sys.argv:
		print "Calling: " + str(sys.argv) + " arguments."
		print "-> Starting Training Process"
		# Initialize
		startTime = time.time() # Starts Timer
		getData() # Gets data from all the files
		rankWords(training_data, "Training Data") # Ranks Words by how many times they appear
		rankWords(validation_data, "Validation Data") # Ranks Words by how many times they appear
		feature_list = [[0 for x in range(len(vocabulary_list))] for y in range(5000)]
		featureWord(all_data, "Training & Validation Data") # Transform into features, input vectors
		createVector()
		# Run Pegasos
		lambd = float(math.pow(2, -3))
		if "-lambd" in sys.argv:
			key = sys.argv.index('-lambd') + 1
			lambd = float(math.pow(2, float(sys.argv[key])))
			print "		-> Lambda set to: " + str(lambd)
		iteration = 20
		if "-iteration" in sys.argv:
			key = sys.argv.index('-iteration') + 1
			iteration = int(float(sys.argv[key]))

		weight = pegasos_svm_train(train_vector_list, all_data, lambd, iteration)
		print "	-> Test Error:"
		pegasos_svm_test(weight, validate_vector_list, validation_data)
		print "	-> Validation Error:"
		pegasos_svm_test(weight, train_vector_list, feature_list)
		'''
		# To get 2^-9 -> 2^1 for Hinge Loss Average	
		weight9 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -9)), iteration)
		pegasos_svm_test(weight9, validate_vector_list, validation_data)
		weight8 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -8)), iteration)
		pegasos_svm_test(weight8, validate_vector_list, validation_data)
		weight7 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -7)), iteration)
		pegasos_svm_test(weight7, validate_vector_list, validation_data)
		weight6 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -6)), iteration)
		pegasos_svm_test(weight6, validate_vector_list, validation_data)
		weight5 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -5)), iteration)
		pegasos_svm_test(weight5, validate_vector_list, validation_data)
		weight4 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -4)), iteration)
		pegasos_svm_test(weight4, validate_vector_list, validation_data)
		weight3 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -3)), iteration)
		pegasos_svm_test(weight3, validate_vector_list, validation_data)
		weight2 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -2)), iteration)
		pegasos_svm_test(weight2, validate_vector_list, validation_data)
		weight1 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, -1)), iteration)
		pegasos_svm_test(weight1, validate_vector_list, validation_data)
		weight0 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, 0)), iteration)
		pegasos_svm_test(weight0, validate_vector_list, validation_data)
		weight10 = pegasos_svm_train(train_vector_list, all_data, (math.pow(2, 1)), iteration)
		pegasos_svm_test(weight10, validate_vector_list, validation_data)
		'''
		print "-> Whole Training Algorithm: Took " + str(time.time() - startTime) + " seconds"
	elif "-test" in sys.argv:
		print "Calling: " + str(sys.argv) + " arguments."
		print "-> Starting Test Process"
		# Initialize
		startTime = time.time() # Starts Timer
		getData() # Gets data from all the files
		rankWords(training_data, "Training Data") # Ranks Words by how many times they appear
		rankWords(validation_data, "Validation Data") # Ranks Words by how many times they appear
		feature_list = [[0 for x in range(len(vocabulary_list))] for y in range(5000)]
		featureWord(all_data, "Training & Validation Data") # Transform into features, input vectors
		# Run Pegasos
		lambd = float(math.pow(2, -3))
		if "-lambd" in sys.argv:
			key = sys.argv.index('-lambd') + 1
			lambd = float(math.pow(2, float(sys.argv[key])))
			print "		-> Lambda set to: " + str(lambd)
		iteration = 20
		if "-iteration" in sys.argv:
			key = sys.argv.index('-iteration') + 1
			iteration = int(float(sys.argv[key]))
		weight = pegasos_svm_train(feature_list, all_data, lambd, iteration)
		print "	-> Test Error:"
		pegasos_svm_test(weight, test_data, test)
		print "	-> Validation Error:"
		pegasos_svm_test(weight, train_vector_list, feature_list)
		print "-> Whole Test Algorithm: Took " + str(time.time() - startTime) + " seconds"
	else:
		print "	-run: to run training"
		print "	-test: to run testing"
		print "	-all: to show all function"
		print " -lambd: to input lambda (default: -5)"
		print " -iteration: to input iterations to make (default: 5)"