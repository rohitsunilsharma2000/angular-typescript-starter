const routes = [
  { path: 'home', loadChildren: 'home.module' },
  { path: 'patients', loadChildren: 'patients.module' },
  { path: 'billing', loadChildren: 'billing.module' }
];
console.log('Lazy routes:', routes);
console.log('Bundle split idea: one chunk per feature.');
