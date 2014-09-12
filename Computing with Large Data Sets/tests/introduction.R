# Introduction to R in Lecture 2

# Adds 100th of a standard deviation
# Increases randomness in a vector
# It jitters it a little
y <- c(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
y <- jitter(y)

# Class of an objects
class(y)
length(y)
# If it has columns and rows
dim(y)

z <- matrix(sample(y), nrow = 5, ncol = 5)
dim(z)
length(z) # Not particularly useful in this case
summary(z)

# check plot.R

# When you do
character(40) # it creates 40 character vector
logical(40) # etc.

# You can do things like
as.numeric(z)