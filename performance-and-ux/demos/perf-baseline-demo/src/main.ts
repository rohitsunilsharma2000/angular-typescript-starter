// Mock baseline targets + fake lighthouse results
const targets = { lcpMs: 2500, inpMs: 200, cls: 0.1, jsKb: 250, cssKb: 80 };
const runResult = { lcpMs: 2400, inpMs: 180, cls: 0.07, jsKb: 230, cssKb: 70 };

function compare(target: number, actual: number, label: string) {
  const pass = actual <= target;
  console.log(`${label}: ${actual} (target <= ${target}) ${pass ? '✅' : '❌'}`);
}

console.log('Perf targets:', targets);
console.log('Fake lab run:', runResult);
compare(targets.lcpMs, runResult.lcpMs, 'LCP ms');
compare(targets.inpMs, runResult.inpMs, 'INP ms');
compare(targets.cls * 1000, runResult.cls * 1000, 'CLS x1000');
