const x = 1;
const r = (function(x: number) { return 2; })(3) + x;
console.log(r);

const x = 1;
const g = function(y: number) { return x; };
const h = function(x: number) { return g(2); };
console.log(h(3));

const x = 42;
const plus = function(x: number, y: number) { return x + y; };
console.log(plus(3, 4));
