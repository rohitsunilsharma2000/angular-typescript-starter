const dod = [
  'Unit + integration green',
  'E2E smoke green',
  'a11y scan 0 violations',
  'Lint/TS strict clean',
  'Visual diff approved (if enabled)'
];
console.log('Testing Definition of Done:');
dod.forEach((d,i)=>console.log(`${i+1}. ${d}`));
