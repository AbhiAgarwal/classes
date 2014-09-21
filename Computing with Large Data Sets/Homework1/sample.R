## exmple code for homework 1 
library(vioplot)

pause <- function() {
	cat("Pause. Press <Enter> to continue...")
	readline()
	invisible()
}


bg.surv <- read.csv(file = "big-data-survey-2014-fall-interests.csv")

rownames(bg.surv) <- bg.surv[, 1]

heatmap(as.matrix(bg.surv[, 8:21]), scale = "none", col = gray(10:0/11))

heatmap(as.matrix(bg.surv[, c(8:12, 14:21)]), scale = "none", col = gray(10:0/11))


summary(as.matrix(bg.surv[, 8:21]))

vioplot(bg.surv[, 8], bg.surv[, 9], bg.surv[, 10], bg.surv[, 11], bg.surv[, 12], bg.surv[, 
	13], bg.surv[, 14], bg.surv[, 15], bg.surv[, 16], bg.surv[, 17], bg.surv[, 18], bg.surv[, 
	19], bg.surv[, 20], bg.surv[, 21], names = colnames(bg.surv[, 8:21]), col = "lightgray")

boxplot(as.matrix(bg.surv[, 8:21]))

panel.cor <- function(x, y, digits = 2, prefix = "", cex.cor) {
	usr <- par("usr")
	on.exit(par(usr))
	par(usr = c(0, 1, 0, 1))
	r <- abs(cor(x, y))
	txt <- format(c(r, 0.123456789), digits = digits)[1]
	txt <- paste(prefix, txt, sep = "")
	if (missing(cex.cor)) 
		cex.cor <- 0.8/strwidth(txt)
	text(0.5, 0.5, txt, cex = 1)
}
panel.hist <- function(x, ...) {
	usr <- par("usr")
	on.exit(par(usr))
	par(usr = c(usr[1:2], 0, 1.5))
	h <- hist(x, plot = FALSE)
	breaks <- h$breaks
	nB <- length(breaks)
	y <- h$counts
	y <- y/max(y)
	rect(breaks[-nB], 0, breaks[-1], y, col = "cyan", ...)
}
pairs(jitter(as.matrix(bg.surv[, c(8:12, 14:21)]), amount = 0.5), lower.panel = panel.smooth, 
	upper.panel = panel.cor, cex = 0.2, diag.panel = panel.hist, cex.labels = 1, font.labels = 1)

h.surv <- hclust(dist(as.matrix(bg.surv[, c(8:12, 14:21)])), method = "ward.D")
cutree(h.surv, k = 15)

protoGroups <- cutree(h.surv, k = 15)

for (i in 1:range(protoGroups)[2]) {
	#print(protoGroups[protoGroups == i] )
	cat("Group: ", i, "   ---------------\n")
	print(bg.surv[names(protoGroups[protoGroups == i]), ])
	cat("\n\n")
	pause()
}
