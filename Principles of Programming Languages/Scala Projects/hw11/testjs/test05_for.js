const plus = function (x: number, y: number) { return x + y };

console.log( plus(3, 4) );


const for =
  function (f: (number, number) => number) {
    return function reduce(n: number, acc: number): number {
      return n === 0 ? acc : reduce(n - 1, f(n, acc))
    }
  };

const factorial =
  function (n: number) {
    return for(function (i: number, acc: number) {
      return i * acc
    })(n, 1)
  };

console.log( factorial(4) );
console.log( factorial(5) );
