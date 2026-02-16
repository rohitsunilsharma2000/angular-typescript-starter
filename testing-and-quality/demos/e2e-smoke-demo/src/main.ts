const smoke = ['login', 'view patients list', 'open patient detail', 'logout'];
console.log('E2E smoke steps:');
smoke.forEach((s,i)=>console.log(`${i+1}. ${s}`));
console.log('Use Playwright/Cypress to automate these steps.');
