# 03) Objects, prototypes, classes — patient & admission

**কি শিখবেন**
- Object literals vs constructor functions vs class syntax
- `this` binding basics
- Prototype method reuse

**Code**
```js
// Constructor + prototype
function Patient(id, name) {
  this.id = id;
  this.name = name;
  this.visits = [];
}
Patient.prototype.addVisit = function (date) {
  this.visits.push(date);
};

// Class sugar (same idea)
class Admission {
  constructor(patient, room) {
    this.patient = patient;
    this.room = room;
  }
  summary() {
    return `${this.patient.name} in ${this.room}`;
  }
}

const p = new Patient('P-21', 'Mina');
p.addVisit('2026-02-16');
console.log(p.visits.length); // 1

const adm = new Admission(p, 'ICU-4');
console.log(adm.summary());
```

**Interview takeaways**
- Classes are syntax sugar over prototypes; methods live on prototype, not per-instance.  
- `new` creates an object linked to the constructor’s prototype.  
- Arrow functions are not ideal for prototype methods (no own `this` binding).  

**Try it**
- Add `discharge()` to `Admission` that nulls `room`.  
- Create a `Doctor` object via literal and via class; compare `instanceof` results.  
- Explain where `addVisit` actually lives in memory.  
