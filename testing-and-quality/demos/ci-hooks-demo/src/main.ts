const hooks = ['pre-commit: lint, test --watch=false', 'pre-push: typecheck', 'CI: lint + test + e2e smoke'];
console.log('Hooks/CI plan:');
hooks.forEach((h,i)=>console.log(`${i+1}. ${h}`));
