const pyramid = [
  { layer: 'Static (lint/types)', target: '100% files', why: 'catch early' },
  { layer: 'Unit', target: 'fast, most cases', why: 'logic correctness' },
  { layer: 'Integration', target: 'critical flows', why: 'wiring' },
  { layer: 'E2E smoke', target: 'happy paths', why: 'real user' }
];
console.log('Testing pyramid (critical path):');
pyramid.forEach(p => console.log(`- ${p.layer}: target ${p.target} — ${p.why}`));
