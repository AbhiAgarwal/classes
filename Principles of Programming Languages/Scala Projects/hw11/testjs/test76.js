const g = function f(x: number): number { return x === 0 ? 1 : x * f(x - 1) };
(function(x: number) {
  const f = g;
  return f(x) + 0.1
})(8)
