const x = 4; 
const y = 3 < 2 || x !== 8 ? (console.log("then"), 7) : console.log("else") + 3; // type error
console.log(y * 6);
