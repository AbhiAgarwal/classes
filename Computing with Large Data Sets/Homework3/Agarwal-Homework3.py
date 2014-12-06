import csv, unicodedata

# Ebola
ebola = open('Ebola_frequencies.csv')
csv_ebola = csv.reader(ebola)

for row in csv_ebola:
	print type(row[0])

# IfTheyGunnedMeDown_frequencies
gunned = open('Ebola_frequencies.csv')
csv_gunned = csv.reader(gunned)

# USTop10Cities_frequencies
city = open('Ebola_frequencies.csv')
csv_city = csv.reader(city)