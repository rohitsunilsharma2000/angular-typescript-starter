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

**Try it**
- loop with `for (var i...)` logging async setTimeout; then fix with `let` to see captured value.  
- Build `makeBedAllocator()` that remembers last assigned bed via closure.  
- Explain TDZ to a friend using the `bed` example above.  
