const checklist = [
  'Lighthouse budget pass',
  'INP < 200ms in field',
  'CLS < 0.1',
  'Critical routes lazy-loaded',
  'Images sized/lazy',
  'TrackBy set on lists'
];

console.log('Perf Definition of Done:');
checklist.forEach((c,i)=>console.log(`${i+1}. ${c}`));
