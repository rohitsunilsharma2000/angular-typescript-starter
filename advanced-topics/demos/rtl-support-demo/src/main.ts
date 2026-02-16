function applyDir(dir: 'ltr' | 'rtl') {
  const style = dir === 'rtl' ? 'direction:rtl;text-align:right' : 'direction:ltr;text-align:left';
  return `<div style="${style}">Hello (dir=${dir})</div>`;
}

console.log(applyDir('ltr'));
console.log(applyDir('rtl'));
