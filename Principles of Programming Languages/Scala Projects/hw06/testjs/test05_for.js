const plus =
  function (x) { return function (y) { return x + y } };

console.log( plus(3)(4) );

const forloop =
  function (body) {
    return function reduce(n) {
      return function (acc) {
        return n === 0 ? acc : body(n)(reduce(n - 1)(acc))
      }
    }
  };

const factorial =
  function (n) {
    return forloop(function (i) {
      return function (acc) {
        return i * acc
      }
    })(n)(1)
  };

console.log( factorial(4) );
console.log( factorial(5) );
