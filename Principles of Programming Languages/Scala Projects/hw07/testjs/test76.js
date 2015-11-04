const g = function (x) { return x === 0 ? 1 : x * f(x - 1) };
(function(x) {
  const f = g;
  return f(x) + 0.1
})(8)
