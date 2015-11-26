const f = function f(x: number): number { return x === 0 ? 1 : x * f(x - 1) };
f(8) + 0.1
