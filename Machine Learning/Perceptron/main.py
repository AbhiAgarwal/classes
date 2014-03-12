'''
The Data has been removed:
Against class policy
If you'd like to see the data with the work - 
contact me (Twitter @AbhiAgarwal)
'''

import copy # copying dict/array function
from numpy import dot # dot function
import time # time (to measure)

training_data = [] # data to train the program
validation_data = [] # data to validate the training
test_data = [] # data to test the whole program
vocabulary_list = {} # vocabulary/word list
weight_list = {} # weight vector
feature_list = [] # email feature list
feature_list_validation = [] # validation set
feature_list_test = []

# Gets single data set from the given data
# Usually require a split data set for all words
# 'Bat', 'hello', rather than "bat hello". So the split
# helps to resolve that very easily, and removing the 1
# and 0 so they don't come into the calculation for ranking.
def getSingleDataSet(data):
	data_set = data.split() # splits the giant array/dict
	counter = 0 # sets counter to 0
	for data in data_set: # goes through the data set 
		if data == '1': # deletes 1, cleanup
			del data_set[counter]
		elif data == '0': # deletes 0, cleanup
			del data_set[counter]
		counter += 1 # increases counter
	return data_set

# Calling the information from the files
# Gets the training, validation, and test data
# Splits train data (5000) into 2 -> 
# 1000 for validation, and 4000 for training
def getData():
	global training_data # Edit the global varabile
	global validation_data # Edit the global varabile
	global test_data # Edit the global varabile
	train = open('', 'r') # Declaration of files
	test = open('', 'r') # Declaration of files
	n = 0 # Counter for 0-4000, 4000-5000
	for data in train: # Getting data from the files
		if data:
			if n < 4000: # Getting 0-3999 data to training set
				training_data.append(data)
				n += 1
			else: # Getting 4000-5000 data to validation set
				validation_data.append(data)
				n += 1
	for testdata in test: # Getting test data from the files
		if testdata: # Getting 0-1000 data to test set
			test_data.append(testdata)
	return 

# Getting word list out of the arrays
# Gets all the words that are most frequent
# and puts them into a seperate dict: vocabulary_list
# The words have to occur 30 or more times to be in this
# dictionary.
def rankWords():
	global vocabulary_list # Edit the global varabile
	global weight_list # weight vector
	vocabulary_list_before = {} # before you do < 30 choosings
	for i in range(0, 4000): # goes through the first 4000 words
		# splits the word set of the training data
		word_set = getSingleDataSet(training_data[i])
		for word in word_set:
			if word in vocabulary_list_before:
				# adds if the word is there
				vocabulary_list_before[word] += 1
			else:
				vocabulary_list_before[word] = 1 # if it hasn't been created
			weight_list = vocabulary_list_before # set one instance of the vocabulary list as the weight
	for i in vocabulary_list_before: # goes through all words above 30 appearances or more
		if vocabulary_list_before[i] >= 30: # puts values of above 30 into the vocabulary list
			vocabulary_list[i] = vocabulary_list_before[i]
	copyAsWeightVector() # Copy training_data -> Weight Vector so they can be weighted
	return

# Copies the vocabulary list and sets it as the weight vector
# and sets each weight to 0
# Required as we need to adjust and get the weight of each word
def copyAsWeightVector():
	global weight_list # Edit the global variable
	for i in weight_list:
		weight_list[i] = 0
	return

def rankWordsValidationSet():
	global vocabulary_list # Edit the global varabile
	global weight_list # weight vector
	vocabulary_list_before = {} # before you do < 30 choosings
	for i in range(0, 1000): # goes through the first 4000 words
		# splits the word set of the training data
		word_set = getSingleDataSet(validation_data[i])
		for word in word_set:
			if word in vocabulary_list_before:
				# adds if the word is there
				vocabulary_list_before[word] += 1
			else:
				vocabulary_list_before[word] = 1 # if it hasn't been created	
	for i in vocabulary_list_before: # goes through all words above 30 appearances or more
		if i in weight_list:
			weight_list[i] += vocabulary_list_before[i]
		else:
			weight_list[i] = vocabulary_list_before[i]
		if vocabulary_list_before[i] >= 30: # puts values of above 30 into the vocabulary list
			if i in vocabulary_list:
				vocabulary_list[i] += vocabulary_list_before[i]
			else:
				vocabulary_list[i] = vocabulary_list_before[i]
	copyAsWeightVectorValidation() # Copy training_data -> Weight Vector so they can be weighted
	return

# Copies the vocabulary list and sets it as the weight vector
# and sets each weight to 0
# Required as we need to adjust and get the weight of each word
def copyAsWeightVectorValidation():
	global weight_list # Edit the global variable
	for i in range(3999, 5000):
		weight_list[i] = 0
	return

def rankWordsTestSet():
	global vocabulary_list # Edit the global varabile
	global weight_list # weight vector
	vocabulary_list_before = {} # before you do < 30 choosings
	for i in range(0, 1000): # goes through the first 4000 words
		# splits the word set of the training data
		word_set = getSingleDataSet(test_data[i])
		for word in word_set:
			if word in vocabulary_list_before:
				# adds if the word is there
				vocabulary_list_before[word] += 1
			else:
				vocabulary_list_before[word] = 1 # if it hasn't been created	
	for i in vocabulary_list_before: # goes through all words above 30 appearances or more
		if i in weight_list:
			weight_list[i] += vocabulary_list_before[i]
		else:
			weight_list[i] = vocabulary_list_before[i]
		if vocabulary_list_before[i] >= 30: # puts values of above 30 into the vocabulary list
			if i in vocabulary_list:
				vocabulary_list[i] += vocabulary_list_before[i]
			else:
				vocabulary_list[i] = vocabulary_list_before[i]
	return

# Determining if the current emails is spam or not
# Just checks the first two characters of the particular email
# Returns 1 if Spam
# Returns 0 if Not Spam
def spamornot(word_list, number):
	toBeDetermined = word_list[number] # checks the word to be determined
	if toBeDetermined[:2].replace(" ", "") == "1": # checks if the first "x " is 1 or 0
		return 1 # returns 1 if spam
	elif toBeDetermined[:2].replace(" ", "") == "0":
		return 0 # returns 0 if not spam

# "For each email, transform it into a feature vector x
# where the ith entry, xi, is 1 if the ith vector in the vocabulary
# occurs in the email, and 0 otherwise."
def featureWord(email_list):
	global feature_list # Edit the global varabile
	for i in range(0, 4000): # Doing this for EACH email
		word_set = getSingleDataSet(email_list[i]) # Get Single data set of the word_set
		feature_list.append([]) # Adds one instance of a multidimensional array
		for word in word_set: # traverse through each word
			if word in vocabulary_list: # If the word is in the vocabulary list
				feature_list[i].append(1) # then append '1' to the end of the Feature List
			else:
				feature_list[i].append(0) # else append '0' to the end of the Feature List
	return

def featureWordValidation(email_list):
	global feature_list_validation # Edit the global varabile
	for i in range(0, 1000): # Doing this for EACH email
		word_set = getSingleDataSet(email_list[i]) # Get Single data set of the word_set
		feature_list_validation.append([]) # Adds one instance of a multidimensional array
		for word in word_set: # traverse through each word
			if word in vocabulary_list: # If the word is in the vocabulary list
				feature_list_validation[i].append(1) # then append '1' to the end of the Feature List
			else:
				feature_list_validation[i].append(0) # else append '0' to the end of the Feature List
	return

def featureWordTest(email_list):
	global feature_list_test # Edit the global varabile
	for i in range(0, 1000): # Doing this for EACH email
		word_set = getSingleDataSet(email_list[i]) # Get Single data set of the word_set
		feature_list_test.append([]) # Adds one instance of a multidimensional array
		for word in word_set: # traverse through each word
			if word in vocabulary_list: # If the word is in the vocabulary list
				feature_list_test[i].append(1) # then append '1' to the end of the Feature List
			else:
				feature_list_test[i].append(0) # else append '0' to the end of the Feature List
	return

# Updates toe weight set according to the new w,
# y, and set
def updateWeightSet(yFunction, inOrNot, word_set):
	count = 0
	for loop in word_set: # goes through the word_set
		weight_list[loop] += (yFunction * inOrNot[count]) # updates the weight according to the function	
		count += 1 # increase the counter by one
	return

# Trains a perceptron classifier using the examples
# provided to the function.
# Returns: Final classification vector, number of updates (mistakes)
# and number of passes through the data (iterations)
def perceptron_train(data, feature, avg, max_iterations):
	start = time.time()
	iterations = 0 # Number of passes through data
	totalmistakes = 0 # Number of mistakes/updates performed
	dataPoint = 0 # What part of the data are we at
	averageCalculator = 0
	totalWeights = 0
	# traversal through the data
	while True and (iterations != max_iterations):
		errorCounter = 0 # Counter for error in each iteration
		for row in data: # the word_set should be exactly mapped onto inOrNot
			word_set = getSingleDataSet(row) # The current set of words
			inOrNot = feature[dataPoint] # If current word it is in the word_set
			desiredOutput = spamornot(data, dataPoint) # 1 = Spam, 0 = Not Spam
			weight_set = [] # set of all weights for the current row
			for words in word_set: # Traversal through the words to check the weight
				weight_set.append(weight_list[words]) # adding weight to the row
			wFunction = dot(weight_set, inOrNot) # weight function
			if avg:
				for i in weight_set:
					averageCalculator += i
					totalWeights += 1
			result = 1 if wFunction > 0 else 0
			error = desiredOutput - result
			if error != 0: # if not then update using w = w + y(i) * f(x)
				updateWeightSet(error, inOrNot, word_set)
				totalmistakes += 1 # global mistakes
				errorCounter += 1 # local counter
			dataPoint += 1 # counter for internal functions
		print "Mistakes of cycle:", (iterations + 1), "mistakes:", errorCounter # print out mistakes per cycle
		if errorCounter == 0 or iterations >= max_iterations: # if the errorCounter is 0 or more than 15 iterations have been done,
			break # then break out of the while loop
		else: # or add one iteration
			iterations += 1
			dataPoint = 0 # set dataPoint (index) back to zero
			errorCounter = 0
	print "(Q4, Q3) Total Mistakes before algorithm ends:", totalmistakes # print out total mistakes
	print "(Q3) Number of passes through the data (Total Iterations):", (iterations + 1) # print out iterations at the end
	end = time.time() # Time Iterations
	print "Time for Total Iterations:", (end - start)
	if avg:
		averageC = (averageCalculator * 1.0) / (totalWeights * 1.0)
		print "(Q6) Average Weight of all Vectors", averageC
	return

# Test to check the Perceptron Algorithm. It uses the validation_data 
# and finds how many errors it has. Then it divides the total with the
# errors and gives a percentage of error.
def perceptron_test(weight, data, feature_set):
	errorCounter = 0 # Errored Operations
	testCount = 0 # Total operations
	dataPoint = 0 # What part of the data are we at
	for row in data:
		word_set = getSingleDataSet(row)
		inOrNot = feature_set[dataPoint]
		desiredOutput = spamornot(data, dataPoint)
		weight_set = [] # set of all weights for the current row
		for words in word_set: # Traversal through the words to check the weight
			weight_set.append(weight[words]) # adding weight to the row
		wFunction = dot(weight_set, inOrNot) # weight function
		result = 1 if wFunction > 0 else 0 # find result either 1 or 0
		error = desiredOutput - result # y will be either 1 or -1
		if error != 0: # if error is not zero
			errorCounter += 1 # increase the error Count by one
		testCount += 1 # increase the test Count by one
		dataPoint += 1 # increase the data point by one - where we are in the data
	errorTotal = ((errorCounter * 1.0) / (testCount * 1.0)) # calculates the error
	print "(Q4) The Validation Error is", (errorTotal * 100) # prints it as a percentage
	return

# Finds the most positive and most negative weight of the data. 
# It goes through the weights and sorts them, and then prints the first
# fifteen most positive and the fifteen most negative.
def weightAnalysis(weight):
	weight_analysis = weight.copy() # sorts the analysis
	weight_sorted = sorted(weight_analysis.iteritems(), key=lambda item: -item[1])
	print "(Q5) Words with the most positive weights" # prints highest values of weights
	print "1. Weight", weight_sorted[0]
	print "2. Weight", weight_sorted[1]
	print "3. Weight", weight_sorted[2]
	print "4. Weight", weight_sorted[3]
	print "5. Weight", weight_sorted[4]
	print "6. Weight", weight_sorted[5]
	print "7. Weight", weight_sorted[6]
	print "8. Weight", weight_sorted[7]
	print "9. Weight", weight_sorted[8]
	print "10. Weight", weight_sorted[9]
	print "11. Weight", weight_sorted[10]
	print "12. Weight", weight_sorted[11]
	print "13. Weight", weight_sorted[12]
	print "14. Weight", weight_sorted[13]
	print "15. Weight", weight_sorted[14]
	print "(Q5) Words with the most negative weights" # prings the lowest value of weights
	lowest = weight_sorted.__len__()
	print "1. Weight", weight_sorted[lowest - 1]
	print "2. Weight", weight_sorted[lowest - 2]
	print "3. Weight", weight_sorted[lowest - 3]
	print "4. Weight", weight_sorted[lowest - 4]
	print "5. Weight", weight_sorted[lowest - 5]
	print "6. Weight", weight_sorted[lowest - 6]
	print "7. Weight", weight_sorted[lowest - 7]
	print "8. Weight", weight_sorted[lowest - 8]
	print "9. Weight", weight_sorted[lowest - 9]
	print "10. Weight", weight_sorted[lowest - 10]
	print "11. Weight", weight_sorted[lowest - 11]
	print "12. Weight", weight_sorted[lowest - 12]
	print "13. Weight", weight_sorted[lowest - 13]
	print "14. Weight", weight_sorted[lowest - 14]
	print "15. Weight", weight_sorted[lowest - 15]
	return

# The main running function
if __name__ == '__main__':
	getData() # Gets data from the files
	rankWords() # Ranks Words by how many times they appear
	featureWord(training_data) # Transform into features, input vectors
	print feature_list.__len__()
	perceptron_train(training_data, feature_list, False, 14) # Runs main perceptron algorithm
	rankWordsValidationSet() # Puts words onto the vocabulary & weight set
	featureWordValidation(validation_data) # Puts Feature onto Validation Set
	perceptron_test(weight_list, validation_data, feature_list_validation) # Tests before doing training
	weightAnalysis(weight_list) # Analyising the weight both Low and High
	perceptron_test(weight_list, test_data, feature_list_test) # Perceptron Test