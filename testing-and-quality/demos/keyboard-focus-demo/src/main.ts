const focusOrder = ['Skip link', 'Nav', 'Search', 'Main button'];
console.log('Expected focus order (Tab):');
focusOrder.forEach((f,i)=>console.log(`${i+1}. ${f}`));
console.log('Test: simulate tabbing through elements in E2E');
