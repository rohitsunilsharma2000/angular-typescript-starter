// Minimal demo of a few ES2020+ features with hospital data.

const admission = { patient: { id: 'P-5', name: 'Imran' }, room: { name: 'ICU-2' } };
const pendingAdmission = { patient: { id: 'P-9', name: 'Sara' } }; // room missing

const label = admission?.room?.name ?? 'TBD';
const label2 = pendingAdmission?.room?.name ?? 'TBD';
console.log('Room labels:', label, label2);

const vitals = [94, 102, 99, 105];
console.log('Sorted high first (toSorted):', vitals.toSorted((a, b) => b - a));
console.log('Last fever (findLast):', vitals.findLast(v => v >= 100));

// Promise.allSettled demo
const fetchMock = url =>
  new Promise(resolve =>
    setTimeout(() => resolve({ url, status: 200, data: { hr: 88 + Math.random() * 10 } }), 20)
  );

Promise.allSettled([fetchMock('/vitals/1'), fetchMock('/vitals/2')]).then(results => {
  console.log('Vitals (allSettled):', results.map(r => r.value?.data.hr));
});

// Promise.withResolvers (ES2024) — may need Node 20+ --harmony-promise-with-resolvers
if (Promise.withResolvers) {
  const triage = Promise.withResolvers();
  setTimeout(() => triage.resolve({ patientId: 'P-42', priority: 'STAT' }), 10);
  triage.promise.then(evt => console.log('Triage fired:', evt));
} else {
  console.log('Promise.withResolvers not available in this runtime.');
}
