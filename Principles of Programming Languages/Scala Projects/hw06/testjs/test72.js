const g = function(x) { return function(y) { return x && y } };
g(1)(0) + 0.1
