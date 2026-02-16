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

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — object literal with methods  
```js
const doctor = {
  id: 'D-1',
  visit() {
    return `${this.id} is on rounds`;
  },
};
console.log(doctor.visit());
```

2) Beginner — computed property name  
```js
const key = 'department';
const room = { [key]: 'ICU', bed: 'ICU-7' };
console.log(room.department, room.bed);
```

3) Intermediate — Object.create prototype chain  
```js
const patientProto = { kind: 'patient', sayHi() { return `Hi from ${this.id}`; } };
const child = Object.create(patientProto);
child.id = 'P-2';
console.log(child.sayHi(), child.kind);
```

4) Intermediate — static factory on class  
```js
class Patient {
  constructor(id, name) { this.id = id; this.name = name; }
  static from(dto) { return new Patient(dto.pid, dto.fullName); }
}

const p2 = Patient.from({ pid: 'P-3', fullName: 'Lara' });
console.log(p2 instanceof Patient, p2.name);
```

5) Advanced — method borrowed with call/apply  
```js
const logger = { prefix: '[LOG]', log(msg) { console.log(this.prefix, msg); } };
const admission = { prefix: '[ADMIT]' };
logger.log.call(admission, 'Room ready');
logger.log.apply({ prefix: '[DISCHARGE]' }, ['Bed cleaned']);
```

**Try it**
- Add `discharge()` to `Admission` that nulls `room`.  
- Create a `Doctor` object via literal and via class; compare `instanceof` results.  
- Explain where `addVisit` actually lives in memory.  
