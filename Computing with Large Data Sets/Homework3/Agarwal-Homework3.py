import csv, unicodedata, enchant, re
from textblob import TextBlob
import string

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
	nonEnglishWords = 0
	amountOfGunned = 0
	for i in csvFileDataset:
		if eachWord(i[0]):
			reducedList.append(i)
		else:
			nonEnglishWords += 1
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
	return (reducedList, reducedList2)

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

	print sectionOne, sectionTwo, sectionThree, sectionFour

	if not ifAscending and not ifDescendending:
		return 'Sentiment fluctuates over time'
	else:
		if ifDescendending:
			return 'Sentiment is descending over time'
		if ifAscending:
			return 'Sentiment is ascending over time'

# ['term', '2014-10-20', '2014-10-27', '2014-10-06', '2014-10-13']
###########

print ''
print 'Ebola'

# Ebola
ebola = open('Ebola_frequencies.csv')
csv_ebola = csv.reader(ebola)
unreducedList = []
reducedList1, reducedList2 = wordFunction(csv_ebola)

# Sentimental Analysis
print 'Reduced List1:', sentimentFunction(reducedList1)
print 'Reduced List2:', sentimentFunction(reducedList2)

###########

print ''
print 'If They Gunned Me Down'

# IfTheyGunnedMeDown_frequencies
gunned = open('IfTheyGunnedMeDown_frequencies.csv')
csv_gunned = csv.reader(gunned)
reducedList1, reducedList2 = wordFunction(csv_gunned)

# Sentimental Analysis
print 'Reduced List1:', sentimentFunction(reducedList1)
print 'Reduced List2:', sentimentFunction(reducedList2)

###########

print ''
print 'US Top 10 Cities'

# USTop10Cities_frequencies
city = open('USTop10Cities_frequencies.csv')
csv_city = csv.reader(city)
reducedList1, reducedList2 = wordFunction(csv_city)

# Sentimental Analysis
print 'Reduced List1:', sentimentFunction(reducedList1)
print 'Reduced List2:', sentimentFunction(reducedList2)

###########