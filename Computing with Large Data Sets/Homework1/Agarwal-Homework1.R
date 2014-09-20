pause <- function (){
    cat("Pause. Press <Enter> to continue...")
    readline()
    invisible()
}

bg.surv <- read.csv( file = "big-data-survey-2014-fall-interests.csv")
rownames(bg.surv) <- bg.surv[,1]
print(rownames(bg.surv))
