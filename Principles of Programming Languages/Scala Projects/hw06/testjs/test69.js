const w = function w(y) { return y === 0 ? 0.1 : y + w(y - 1) };
w(3)
