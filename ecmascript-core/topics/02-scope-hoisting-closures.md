# 02) Scope, hoisting, closures — EMR access control

**কি শিখবেন**
- function/block scope, hoisting effect
- closure দিয়ে private counter/flag রাখা
- common interview bugs (var leak, TDZ)

**Code**
```js
// Hoisting demo
console.log(room); // undefined (var hoisted)
var room = 'Ward-5';

try {
  console.log(bed); // ReferenceError (TDZ)
  let bed = 'ICU-2';
} catch (e) { console.log('TDZ hit'); }

// Closure for audit counter
function makeAdmissionCounter() {
  let count = 0;
  return function logAdmission(patientId) {
    count++;
    return `Admit #${count}: ${patientId}`;
  };
}
const logAdmit = makeAdmissionCounter();
console.log(logAdmit('P-12')); // Admit #1
console.log(logAdmit('P-13')); // Admit #2
```

**Interview takeaways**
- `var` hoists to function scope; `let/const` block scope + TDZ.  
- Closures capture lexical environment → useful for private state (audit counts).  
- Avoid `var` in loops to prevent leak into outer scope.  

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — block scope vs global leak
```js
{ let nurse = 'Asha'; }
console.log(typeof nurse); // undefined

{ var ward = 'ER'; }
console.log(ward); // 'ER' leaked to function/global scope
```

2) Beginner — var in loop hoisting pitfall
```js
for (var i = 0; i < 3; i++) {
  setTimeout(() => console.log('var i =', i), 10);
}

// Fix with let
for (let j = 0; j < 3; j++) {
  setTimeout(() => console.log('let j =', j), 10);
}
```

3) Intermediate — TDZ check with let
```js
try {
  console.log(bed); // ReferenceError: TDZ
  let bed = 'ICU-1';
} catch (e) {
  console.log('Caught TDZ:', e.name);
}
```

4) Intermediate — closure counter per department
```js
function makeDeptCounter(name) {
  let count = 0;
  return () => `${name} admits: ${++count}`;
}

const icuCount = makeDeptCounter('ICU');
console.log(icuCount());
console.log(icuCount());
```

5) Advanced — configurable logger via closure
```js
const makeLogger = level => message =>
  console.log(`[${level}]`, message);

const warn = makeLogger('WARN');
const info = makeLogger('INFO');
warn('Bed offline');
info('Shift started');
```

**Try it**
- loop with `for (var i...)` logging async setTimeout; then fix with `let` to see captured value.  
- Build `makeBedAllocator()` that remembers last assigned bed via closure.  
- Explain TDZ to a friend using the `bed` example above.  
