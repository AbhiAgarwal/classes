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

# ['term', '2014-10-20', '2014-10-27', '2014-10-06', '2014-10-13']
###########

# Ebola
ebola = open('Ebola_frequencies.csv')
csv_ebola = csv.reader(ebola)

# Check the words, and remove english words
reducedList = []
nonEnglishWords, amountOfEbola = 0, 0
for i in csv_ebola:
	if eachWord(i[0]):
		reducedList.append(i)
	else:
		nonEnglishWords += 1
	amountOfEbola += 1

print "Number of words:", amountOfEbola
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

# Sentimental Analysis
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

if ifDescendending:
	print 'Sentiment is descending over time'
else:
	print 'Sentiment is ascending over time'

###########

# IfTheyGunnedMeDown_frequencies
gunned = open('Ebola_frequencies.csv')
csv_gunned = csv.reader(gunned)

###########

# USTop10Cities_frequencies
city = open('Ebola_frequencies.csv')
csv_city = csv.reader(city)

###########