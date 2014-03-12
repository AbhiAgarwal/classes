'''
The Data has been removed:
Against class policy
If you'd like to see the data with the work - 
contact me (Twitter @AbhiAgarwal)
'''

import sys, time, math, operator, itertools
from copy import deepcopy
train_vectors = [] # list of validation & training vectors
test_vectors = [] # list of test vectors
validation_vectors = [] # training vector
training_vectors = [] # validation vector

# @Gets data from train & test
# @Returns nothing
def getData():
	temp = []
	global train_vectors, test_vectors # Editing the global varabile
	print "		-> Getting Test, Validation & Training Data from {DATA SOURCE}"
	startTime = time.time() # Starts Timer
	train = open('', 'r') # Declaration of files
	test = open('', 'r') # Declaration of files
	print "		-> Starting to get Training & Validation Data"
	for i, sent in enumerate(train):
		del temp[:] # to refresh the queue
		lineOne = sent.split(",")
		temp = [0 for i in range(0, len(lineOne))]
		for x, num in enumerate(lineOne):
			if x > 0:
				vec_math = (((int(num) * 2) / (255)) - 1)
				temp[x] = vec_math
			else:
				pass
		toAppend = [0 for i in range(0, 2)]
		toAppend[0] = int(lineOne[0]) # takes '3', '[-1, 1, 1, -1, 0]' to Append
		toAppend[1] = deepcopy(temp)
		train_vectors.append(toAppend)
	print "		-> Got Training & Validation Data"
	temp = []
	print "		-> Starting to get Test Data"
	for i, sent in enumerate(test):
		del temp[:] # to refresh the queue
		lineOne = sent.split(",")
		temp = [0 for i in range(0, len(lineOne))]
		for x, num in enumerate(lineOne):
			if x > 0:
				vec_math = (((int(num) * 2) / (255)) - 1)
				temp[x] = vec_math
			else:
				pass
		toAppend = [0 for i in range(0, 2)]
		toAppend[0] = int(lineOne[0]) # takes '3', '[-1, 1, 1, -1, 0]' to Append
		toAppend[1] = deepcopy(temp)
		test_vectors.append(toAppend)
	print "			-> Got Test Data"
	print "			-> There are " + str(len(train_vectors)) + " vectors for the Validation Set"
	print "			-> There are " + str(len(test_vectors)) + " vectors for the Test Set"
	print "			-> Get Data Took " + str(time.time() - startTime) + " seconds"
	return

# Splits vector training, and validation into 1600:400 split
# @Returns nothing
def createVector():
	if "-all" in sys.argv:
		print "Calling createVector"
	global validation_vectors, training_vectors
	validation_vectors = train_vectors[1:1600]
	training_vectors = train_vectors[1600:2000]
	return

# Adds 2 Vectors Together
# @ Returns added vector
def vectorAdd(vecOne, vecTwo):
	if "-all" in sys.argv:
		print "Calling vectorAdd"
	vec = []
	for one, two in zip(vecOne, vecTwo):
		vec.append(one + two)
	return vec

# Multiplies one Scalar and one Vector
# @Returns Multiplied Number
def vectorMult(scalarQ, vecAr):
	if "-all" in sys.argv:
		print "Calling vectorMult"
	for index, vector in enumerate(vecAr):
		vecAr[index] *= scalarQ
	return vecAr

# Performs dot product on two Vectors
# @Returns Dot
def dotFunction(vecOne, vecTwo):
	if "-all" in sys.argv:
		print "Calling dotFunction"
	return sum(one * two for one, two in zip(vecOne, vecTwo))

# Gets magnitude of a vector
# @Returns magnitude
def vectorMagnitude(vector):
	if "-all" in sys.argv:
		print "Calling vectorMagnitude"
	div = float(0)
	for values in vector:
		div += float(math.pow(values, 2))
	magnitude = float(math.sqrt(div))
	return 0.001 if (div == 0.0) else magnitude

# Evaluates w/ weight, lambda, and next(two)
# @Returns evaluated object
def evaluateFunc(weight, lambd, two):
	if "-all" in sys.argv:
		print "Calling evaluateFunc"
	obj = ((lambd / 2) * math.pow(vectorMagnitude(weight), 2)) + two
	return obj

# @Trains using Support Vector Machine Algorithm
# @Returns final classification vector
def pegasos_svm_train(data, lambd, iteration):
	print "		-> Starting to Train Pegasos Algorithm"
	startTime = time.time() # Starts Timer
	base_0 = data[0]
	base_1 = base_0[1]
	iterations = 1
	weights_pegasos = [0 for i in range(0, len(base_1))]
	u = [0 for i in range(0, len(base_1))]
	t = 0
	nt = 0
	for i in range(0, iteration): # 20 passes through data
		overone = 0 # > 1
		underone = 0 # < 1
		evalution = 0 # eval
		for index, vector in enumerate(data): # Traversal through the Feature List
			yj = vector[0]
			vec = vector[1]
			t += 1
			nt = ((float(1))/(t * lambd))
			if((yj * dotFunction(weights_pegasos, vec)) < 1):
				u = vectorAdd((vectorMult((1 - (nt * lambd)), weights_pegasos)), (vectorMult((nt * yj), vec)))
				underone += 1
			else:
				u = vectorMult((1 - (nt * lambd)), (weights_pegasos))
				overone += 1
			val = min(1, ((1 / (math.sqrt(lambd))) / (vectorMagnitude(u))))
			tempo = vectorMult(val, u)
			weights_pegasos = tempo[:]
			evalution += max(0, (1 - (nt * dotFunction(weights_pegasos, vec))))
			del u[:] #reset
		obj_val = evaluateFunc(weights_pegasos, lambd, evalution)
		print "			-> Lower Than One: " + str(underone) + " Over One: " + str(overone)
		print "			-> Number: " + str(iterations) + " Value: " + str(obj_val)
		iterations += 1
	print "			-> Took " + str(time.time() - startTime) + " seconds"
	return weights_pegasos

	sys.exit(1)
	print "			-> Pegasos SVM Took " + str(time.time() - startTime) + " seconds"

# Performs & Sorts data to put into the "pegasos_svm_train" algorithm
# @Returns evaluted "pegasos_svm_train" w/ altered "multiclass-pegasos" data
def multipegasos(data, lambd, classificationNum, dataset, iterations):
	print "		-> Starting to parse data for Pegasos Algorithm"
	print "		-> Dataset: " + str(dataset)
	print "		-> Classification Number: " + str(classificationNum)
	startTime = time.time() # Starts Timer
	temp = [() for i in range(0, len(data))]
	classification = int(classificationNum) # make sure not string (error checking)
	for i, val in enumerate(data):
		temp_x = 0 #initialize
		if(int(val[0]) != classification):
			temp_x = (-1, val[1])
		else:
			temp_x = (1, val[1])
		temp[i] = deepcopy(temp_x)
	print "		-> Calling Pegasos SVM to start training"
	print "			-> Multi Took " + str(time.time() - startTime) + " seconds"
	return (pegasos_svm_train(temp, lambd, iterations))

# Tests the Pegasos SVM Algorithm to see how well the data did
# @Returns how many times it misses, but prints out the final result
def multipegasostest(data, weights, classifications, dataset):
	print "		-> Starting to parse data for Pegasos Tester Algorithm"
	print "		-> Dataset: " + str(dataset)
	startTime = time.time() # Starts Timer
	errors = 0
	hand = []
	for i, val in enumerate(data):
		variable_x = val[0] # Placeholder for x when x is [x, [0,1,-1,1,0...]]
		vector_x = val[1] # Vector [variable_x, [vector_x]]
		hand = [0 for i in range(0, len(weights))] # initialize hand
		for x, weightOne in enumerate(weights):
			hand[x] = dotFunction(weightOne, vector_x)
		max_x, max_y = max((enumerate(hand)), key = operator.itemgetter(1))
		if((int(classifications[max_x])) != (int(variable_x))):
			errors += 1
	notRight = (float(errors) / (float(len(data))))
	print "			-> Error: " + str(errors) + ", out of: " + str(len(data)) + ". Has " + str(notRight * 100) + "% errors"
	print "			-> Took " + str(time.time() - startTime) + " seconds"	
	return errors

# @Main function
# @Returns nothing
if __name__ == '__main__':
	# Initializes the program
	print "Calling: " + str(sys.argv) + " arguments."
	# Starts Timer
	startTime = time.time()
	# Start Process for user input
	# Gets all common values for each, and sets them
	# Get Lambda
	lambd = float(math.pow(2, -3))
	if "-lambd" in sys.argv:
		key = sys.argv.index('-lambd') + 1
		lambd = float(math.pow(2, float(sys.argv[key])))
		print "		-> Lambda set to: " + str(lambd)
	# Get the classification index you want
	classification_index = []
	classification_index = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
	if "-classification" in sys.argv:
		key = sys.argv.index('-classification') + 1
		classification_index = list(sys.argv[key])
		classification_index = map(int, classification_index)
		if (len(classification_index) != 10):
			classification_index = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
			print "		-> *** Classification Size Error: set to default"
		# Get number of Iterations
	iteration = 5
	if "-iteration" in sys.argv:
		key = sys.argv.index('-iteration') + 1
		iteration = int(float(sys.argv[key]))
	divide = 5
	if "-divide" in sys.argv:
		key = sys.argv.index('-divide') + 1
		divide = int(float(sys.argv[key]))
	getData()
	if "-run" in sys.argv:
		print "-> Starting Training & Validation Process"
		# Runs Multi Pegasos
		# Calculate 10 weights
		weight0 = multipegasos(train_vectors, lambd, classification_index[0], "Training Data", iteration)
		weight1 = multipegasos(train_vectors, lambd, classification_index[1], "Training Data", iteration)
		weight2 = multipegasos(train_vectors, lambd, classification_index[2], "Training Data", iteration)
		weight3 = multipegasos(train_vectors, lambd, classification_index[3], "Training Data", iteration)
		weight4 = multipegasos(train_vectors, lambd, classification_index[4], "Training Data", iteration)
		weight5 = multipegasos(train_vectors, lambd, classification_index[5], "Training Data", iteration)
		weight6 = multipegasos(train_vectors, lambd, classification_index[6], "Training Data", iteration)
		weight7 = multipegasos(train_vectors, lambd, classification_index[7], "Training Data", iteration)
		weight8 = multipegasos(train_vectors, lambd, classification_index[8], "Training Data", iteration)
		weight9 = multipegasos(train_vectors, lambd, classification_index[9], "Training Data", iteration)
		# Form all Weights
		# into Array, and sets the classification
		allWeights = (weight0, weight1, weight2, weight3, weight4, weight5, weight6, weight7, weight8, weight9)
		errors = multipegasostest(test_vectors, allWeights, classification_index, "Test Data")
		# finishing off the Pegasos
		print "-> Whole Algorithm: Took " + str(time.time() - startTime) + " seconds"
	elif "-cross" in sys.argv:
		print "Calling: " + str(sys.argv) + " arguments."
		print "-> Starting Cross Validation Process"
		startTime = time.time() # Starts Timer
		errors = 0
		print "		-> Chosen to divided data into " + str(divide) + " blocks."
		itemsPerDivision = ((len(train_vectors)) / (divide))
		divide_data = [[] for i in range(0, divide)] #splits into divide
		divide_process_begin = 0
		for x in range(0, divide):
			final_divide_process = itemsPerDivision + divide_process_begin
			divide_data[x - 1] = train_vectors[divide_process_begin:final_divide_process]
			divide_process_begin = final_divide_process # traversal in blocks
		print "		-> Split into " + str(divide) + " blocks of " + str(itemsPerDivision) + " each!"
		data_x = []
		for x in range(0, divide):
			del data_x[:]
			for y in range(0, divide):
				if y != x:
					data_x = list(itertools.chain(data_x, divide_data[y]))
				else:
					pass
			pid = (x+1)
			print "			-> At process: " + str(pid)
			weight0 = multipegasos(data_x, lambd, classification_index[0], str("Training Data: Process ID: " + str(pid)), iteration)
			weight1 = multipegasos(data_x, lambd, classification_index[1], str("Training Data: Process ID: " + str(pid)), iteration)
			weight2 = multipegasos(data_x, lambd, classification_index[2], str("Training Data: Process ID: " + str(pid)), iteration)
			weight3 = multipegasos(data_x, lambd, classification_index[3], str("Training Data: Process ID: " + str(pid)), iteration)
			weight4 = multipegasos(data_x, lambd, classification_index[4], str("Training Data: Process ID: " + str(pid)), iteration)
			weight5 = multipegasos(data_x, lambd, classification_index[5], str("Training Data: Process ID: " + str(pid)), iteration)
			weight6 = multipegasos(data_x, lambd, classification_index[6], str("Training Data: Process ID: " + str(pid)), iteration)
			weight7 = multipegasos(data_x, lambd, classification_index[7], str("Training Data: Process ID: " + str(pid)), iteration)
			weight8 = multipegasos(data_x, lambd, classification_index[8], str("Training Data: Process ID: " + str(pid)), iteration)
			weight9 = multipegasos(data_x, lambd, classification_index[9], str("Training Data: Process ID: " + str(pid)), iteration)
			# Form all Weights
			# into Array, and sets the classification
			allWeights = (weight0, weight1, weight2, weight3, weight4, weight5, weight6, weight7, weight8, weight9)
			errors += multipegasostest(divide_data[x], allWeights, classification_index, "Test Data")
		error_avg = ((float(errors))/(float(divide)))
		half_error_avg = (float(errors) / float(len(train_vectors)))
		final_error_avg = ((float(half_error_avg)) / (float(divide)))
		print "		-> Average errors in classification is " + str(error_avg) + ", " + str(100 * (error_avg/1000))
		print "		-> Average errors in validating data is " + str(final_error_avg * 100) + "%."
		# Error comes out now
		# finishing off the Pegasos
		print "		-> Whole Algorithm: Took " + str(time.time() - startTime) + " seconds"
	else:
		print "	Run:"
		print "	-run: to run training"
		print "	-run -lambd: to input lambda (default: -3)"
		print "	-run -iteration: to input iterations to make (default: 5)"
		print "	-run -classification: to enter classifications, enter as 0123456789 (joint), (default: 0123456789)"
		print "	-run -all: to show all function output (each math function as well)"
		print "	Cross:"
		print "	-cross: to run the cross validation algorithm"
		print "	-cross -divide: to set how mnay divisions of the data you want"
		print "	-cross -lambd: to input lambda (default: -3)"
		print "	-cross -iteration: to input iterations to make (default: 5)"
		print "	-cross -classification: to enter classifications, enter as 0123456789 (joint), (default: 0123456789)"
		print "	-cross -all: to show all function output (each math function as well)"