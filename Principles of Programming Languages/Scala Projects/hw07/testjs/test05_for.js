const plus = function (x, y) { return x + y };

console.log( plus(3, 4) );

const for =
  function (f) {
    return function reduce(n, acc) {
        return n === 0 ? acc : reduce(n - 1, f(n, acc))
    }
  };

const factorial =
  function (n) {
    return for(function (i, acc) {
        return i * acc
    })(n, 1)
  };

console.log( factorial(4) );
console.log( factorial(5) );
