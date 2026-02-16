const generator = 'nx g @nrwl/angular:component patient-card --project=patients-lib';
const affected = 'nx affected:test --base=main --head=HEAD';
console.log('Generator example:', generator);
console.log('Affected example:', affected);
