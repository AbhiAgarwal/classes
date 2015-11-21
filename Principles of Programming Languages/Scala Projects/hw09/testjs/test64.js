const f = function (x: number) { return x + 2.1 };
false ? f === 7.2 : f(7) // type error
