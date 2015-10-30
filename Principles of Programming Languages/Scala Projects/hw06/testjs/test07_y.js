const fix = 
  function(f) {
    return function (x) {
      return f(function (y) { return (x(x))(y) })
    }
    (function (x) {
      return f(function (y) { return (x(x))(y) })
    })
  };

const facFun = 
  function(factorial) {
    return function (n) {
      return n > 1 ? n * factorial(n-1) : 1
    }
  };

const factorial = fix(facFun);

console.log( factorial(4) );
console.log( factorial(5) );