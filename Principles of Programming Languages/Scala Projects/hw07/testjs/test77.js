const r1 = 1.01;
const r2 = 1.01;
const f = function f(x) { console.log(r1);
                const temp = r2;
                const r2 = r1 + r2;
                const r1 = temp;
                return x === 0 ? 0 : f(x - 1) };
f(5);
console.log(r1);
console.log(r2)
