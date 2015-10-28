const x = 1;
const r = (function(x) { return 2; })(3) + x;
console.log(r);

const x = 1;
const g = function(y) { return x; };
const h = function(x) { return g(2); };
console.log(h(3));

const x = 42;
const plus = function(x) { return function(y) { return x + y; } };
console.log(plus(3)(4));
