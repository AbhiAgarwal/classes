const g = function(x: number, y: number) { return x && y /* type error */};
g(1)(0) + 0.1
