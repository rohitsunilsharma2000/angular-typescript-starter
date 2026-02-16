const budgets = { jsKb: 250, cssKb: 80, imgKb: 150 };
const report = { jsKb: 260, cssKb: 70, imgKb: 120 };

function check(label: keyof typeof budgets) {
  const pass = report[label] <= budgets[label];
  console.log(`${label}: ${report[label]}KB (<= ${budgets[label]}KB) ${pass ? '✅' : '❌'}`);
}

Object.keys(budgets).forEach(k => check(k as any));
