## Word counts dataset
This dataset contains word counts from 3 twitter collections

1. Tweets related to the shooting of Michael Brown in Fergosun: all tweets sent between 2014-10-1 and 2014-11-1 containing one of the keywords (ferguson, mikebrown, justiceformikebrown, blacklivesmatter, blacklifematters, iftheygunnedmedown, fergusonshooting, fergusonriot, nmos14, noangel). 948,847 tweets, 180MB.

2. Tweets sent with geotagging (geo-tagging is optional and most twitter users don't use it), from 10 large US cities (New York, Los Angeles, Chicago, Houston, Philadelphia, Phoenix, Indianapolos, Jacksonville, Columbus, Charlotte), sent between 2014-10-31 and 2014-11-25. 16,755,061 tweets, 2.7GB.

3. Tweets containing the keyword "ebola", sent between 2014-10-1 and 2014-11-1. 13,088,357 tweets, 2.4GB.

For each data set, only words that appear in at least 3 tweets, but no more than 95% of tweets, are counted.

In all 3 cases, only tweets where twitter reports the language is English are counted. Note that the twitter language-decetion isn't leak-proof and some non-english tweets inevitably get counted as well.

Each collection is split into weekly periods and words are counted within each period. The data files are

1. IfTheyGunnedMeDown_frequencies.csv
2. USTop10Cities_frequencies.csv
3. Ebola_frequencies.csv

Each file lists the term counts for each term in each period. It contains 1 column listing the term, and one column per weekly period, named by the first day of that period.