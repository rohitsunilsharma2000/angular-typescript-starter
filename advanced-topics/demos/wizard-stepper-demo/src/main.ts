const steps = ['patient-info', 'appointment', 'confirm'] as const;
let index = 0;

function current() { return steps[index]; }
function next() { if (index < steps.length - 1) index++; return current(); }
function prev() { if (index > 0) index--; return current(); }

console.log('start:', current());
console.log('next:', next());
console.log('next:', next());
console.log('prev:', prev());
