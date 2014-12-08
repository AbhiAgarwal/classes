import csv, unicodedata, enchant, re, collections, sys
from textblob import TextBlob
import string
from random import shuffle

#### ['term', '2014-10-20', '2014-10-27', '2014-10-06', '2014-10-13']

#### Extra Functions ####

a = re.compile("[A-Za-z]^")
d = enchant.Dict("en_US")
allTheLetters = string.lowercase + ' '

def eachWord(text):
	for i in text:
		if i.lower() not in allTheLetters:
			return False
	return True

def sentimentAnalysis(text):
	blob = TextBlob(text)
	return blob.sentiment.polarity

def wordFunction(csvFileDataset):
	# Check the words, and remove english words
	reducedList = []
	reducedList3 = []
	nonEnglishWords = 0
	amountOfGunned = 0
	for i in csvFileDataset:
		if eachWord(i[0]):
			reducedList.append(i)
		else:
			nonEnglishWords += 1
			reducedList3.append(i)
		amountOfGunned += 1

	print "Number of words:", amountOfGunned
	print "Number of words not english foreach letter:", nonEnglishWords

	# How many words not in english dictionary
	reducedList2 = []
	notInEnglishDictionary = 0
	for i in reducedList:
		if not d.check(i[0]):
			notInEnglishDictionary += 1
		else:
			reducedList2.append(i)

	print "Number of words not in english dictionary", notInEnglishDictionary
	return (reducedList, reducedList2, reducedList3)

def sentimentFunction(reducedList2):
	sectionOne, sectionTwo, sectionThree, sectionFour = 0.0, 0.0, 0.0, 0.0
	for i in reducedList2[1:len(reducedList2)]:
		sentimentOfWord = float(sentimentAnalysis(i[0]))
		sectionOne += (sentimentOfWord * float(i[1]))
		sectionTwo += (sentimentOfWord * float(i[2]))
		sectionThree += (sentimentOfWord * float(i[3]))
		sectionFour += (sentimentOfWord * float(i[4]))
	sectionOne /= len(reducedList2)
	sectionTwo /= len(reducedList2)
	sectionThree /= len(reducedList2)
	sectionFour /= len(reducedList2)
	seq = [sectionOne, sectionTwo, sectionThree, sectionFour]
	ifDescendending = all(earlier >= later for earlier, later in zip(seq, seq[1:]))
	ifAscending = all(earlier <= later for earlier, later in zip(seq, seq[1:]))

	#print sectionOne, sectionTwo, sectionThree, sectionFour

	if not ifAscending and not ifDescendending:
		return 'Sentiment fluctuates over time'
	else:
		if ifDescendending:
			return 'Sentiment is descending over time'
		if ifAscending:
			return 'Sentiment is ascending over time'

def mostCommonWords(reducedList1, reducedList2):
	for z in range(1, 5):
		currentYear = reducedList1[0][z]
		print ''
		print currentYear
		print ''
		# Most Frequent Words in different years
		listOfWords = []
		for i in reducedList1[1:len(reducedList1)]:
			for x in range(0, int(i[z])):
				listOfWords.append(i[0])

		listOfWords2 = []
		for i in reducedList2[1:len(reducedList2)]:
			for x in range(0, int(i[z])):
				listOfWords2.append(i[0])

		listOfWordsCounter = collections.Counter(listOfWords)
		print 'Most Common Words:', "reducedList1:", listOfWordsCounter.most_common(5)

		listOfWordsCounter2 = collections.Counter(listOfWords2)
		print 'Most Common Words:', "reducedList2:", listOfWordsCounter2.most_common(5)

def nounPhrase(reducedList2):
	text = ''
	eachDict = {}
	for i in reducedList2[1:len(reducedList2)]:
		if TextBlob(i[0]):
			eachDict[i[0]] = (int(i[1]) + int(i[2]) + int(i[3]) + int(i[4]))

	listOfWords = []
	for i in eachDict:
		for x in range(0, eachDict[i]):
			listOfWords.append(i)

	listOfWordsCounter = collections.Counter(listOfWords)
	print 'Noun Phrases:', listOfWordsCounter.most_common(5)

def findLanguage(reducedList3):
	languageMap = {}
	currentNumber = 0

	shuffle(reducedList3)
	for i in reducedList3:
		if currentNumber < 5000:
			if len(i[0]) > 5:
				try:
					b = TextBlob(unicode(i[0]))
					currentLanguage = b.detect_language()
					if currentLanguage in languageMap:
						languageMap[currentLanguage] += 1
					else:
						languageMap[currentLanguage] = 1
				except: 
					pass
			currentNumber += 1
			print currentNumber

	listOfWords = []
	for i in languageMap:
		for x in range(0, languageMap[i]):
			listOfWords.append(i)

	listOfWordsCounter = collections.Counter(listOfWords)
	print 'Best Languages:', listOfWordsCounter.most_common(5)

	print languageMap

#### Datasets ####

def Ebola():
	print ''
	print 'Ebola'

	# Ebola
	ebola = open('Ebola_frequencies.csv')
	csv_ebola = csv.reader(ebola)
	unreducedList = []
	reducedList1, reducedList2, reducedList3 = wordFunction(csv_ebola)

	"""
	# Sentimental Analysis
	print 'Reduced List1:', sentimentFunction(reducedList1)
	print 'Reduced List2:', sentimentFunction(reducedList2)

	mostCommonWords(reducedList1, reducedList2)
	"""

	"""
	u'eo': 14, u'en': 2746, u'af': 34, u'vi': 29, u'is': 15, u'it': 56, 
	u'eu': 15, u'cy': 25, u'ga': 12, u'et': 29, u'cs': 21, u'zu': 26, 
	u'gl': 11, u'id': 50, u'es': 74, u'az': 3, u'nl': 31, u'pt': 61, 
	u'la': 19, u'tr': 21, u'lt': 18, u'lv': 6, u'tl': 30, u'ig': 6, 
	u'mi': 10, u'ro': 23, u'ca': 14, u'pl': 24, u'mn': 1, u'fr': 69, 
	u'de': 36, u'yo': 13, u'hr': 5, u'jw': 11, u'hu': 13, u'ht': 208, 
	u'da': 20, u'bs': 14, u'fi': 42, u'ha': 24, u'sq': 9, u'no': 39, 
	u'sw': 23, u'sv': 36, u'sk': 15, u'mt': 8, u'so': 7, u'ms': 26, 
	u'sl': 28, None: 7}
	"""

	# nounPhrase(reducedList2)
	#findLanguage(reducedList3)

def IfTheyGunnedMeDown():
	print ''
	print 'If They Gunned Me Down'

	# IfTheyGunnedMeDown_frequencies
	gunned = open('IfTheyGunnedMeDown_frequencies.csv')
	csv_gunned = csv.reader(gunned)
	reducedList1, reducedList2, reducedList3 = wordFunction(csv_gunned)

	"""
	# Sentimental Analysis
	print 'Reduced List1:', sentimentFunction(reducedList1)
	print 'Reduced List2:', sentimentFunction(reducedList2)

	mostCommonWords(reducedList1, reducedList2)
	"""

	"""
	{u'eo': 2, u'en': 383, u'af': 3, u'vi': 3, u'ca': 1, u'it': 6, 
	u'eu': 1, u'cy': 1, u'ga': 1, u'zu': 2, u'cs': 2, u'et': 3, 
	u'az': 1, u'ig': 2, u'nl': 4, u'pt': 9, u'no': 1, u'tr': 2, 
	u'tl': 5, u'lv': 1, u'lt': 2, u'es': 5, u'ro': 1, u'is': 1, 
	u'pl': 5, u'fr': 6, u'jw': 2, u'yo': 1, u'de': 7, u'ht': 31, 
	u'hu': 1, u'bs': 1, u'fi': 2, u'ha': 2, u'da': 4, u'sw': 3, 
	u'sv': 5, u'mt': 2, u'ms': 1, u'sl': 2}
	"""

	# findLanguage(reducedList3)

def USTopTenCities():
	print ''
	print 'US Top 10 Cities'

	# USTop10Cities_frequencies
	city = open('USTop10Cities_frequencies.csv')
	csv_city = csv.reader(city)
	reducedList1, reducedList2, reducedList3 = wordFunction(csv_city)

	"""
	# Sentimental Analysis
	print 'Reduced List1:', sentimentFunction(reducedList1)
	print 'Reduced List2:', sentimentFunction(reducedList2)

	mostCommonWords(reducedList1, reducedList2)
	"""

	findLanguage(reducedList3)

USTopTenCities()