import csv

ebola = open('Ebola_frequencies.csv')
csv_ebola = csv.reader(ebola)

for i in csv_ebola:
	# Basically csv_ebola contains an array of items
	# The first index: i[0] is the word
	# And the rest i[1] i[2] i[3] i[4] correspond to the dates
	print 'Word', i[0], '2014-10-20', i[1], '2014-10-27', i[2], '2014-10-06', i[3], '2014-10-13', i[4]