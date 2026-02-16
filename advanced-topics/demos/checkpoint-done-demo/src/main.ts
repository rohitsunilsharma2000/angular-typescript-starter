const checklist = [
  'Lint + typecheck green',
  'Storybook build passes',
  'i18n+RTL verified',
  'PWA update banner tested',
  'Realtime reconnect handles close',
  'Bundle budgets respected'
];

console.log('Definition of Done checklist:');
checklist.forEach((item, i) => console.log(`${i + 1}. ${item}`));
