const repeat = function(s: string) {
  return function loop(n: number): string {
    return n === 0 ? "" : s + loop(n - 1)
  }
};

console.log('repeat("a")(3)');
console.log(repeat("a")(3));

console.log('repeat("xyz")(2)');
console.log(repeat("xyz")(2))
