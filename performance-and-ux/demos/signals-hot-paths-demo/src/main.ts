// Simulate signals: computed runs only when source changes
let computeRuns = 0;
let state = { count: 0 };

function signal() { return state.count; }
function computed() { computeRuns++; return signal() * 2; }

function setCount(v: number) { state = { count: v }; }

console.log('initial computed:', computed(), 'runs:', computeRuns);
setCount(1); console.log('after set(1):', computed(), 'runs:', computeRuns);
setCount(1); console.log('after set(1) again:', computed(), 'runs:', computeRuns);
setCount(2); console.log('after set(2):', computed(), 'runs:', computeRuns);
