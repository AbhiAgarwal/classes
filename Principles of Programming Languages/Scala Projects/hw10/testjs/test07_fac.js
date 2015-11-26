const fac = function(n: number) {
    var x = 1;
    var i = 1;
    (function facAcc(): Undefined {
       i < n ? (x = x * (i = i + 1), facAcc()) : undefined;
     })();
    return x;
  };
fac(5)