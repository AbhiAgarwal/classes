const repeat = function(s) {
  return function loop(n) {
    return n === 0 ? "" : s + loop(n - 1)
  }
};

console.log('repeat("a")(3)');
console.log(repeat("a")(3));

console.log('repeat("xyz")(2)');
console.log(repeat("xyz")(2))
