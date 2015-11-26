const w = function w(y: number): number { return y === 0 ? 0.1 : y + w(y - 1) };
w(3)
