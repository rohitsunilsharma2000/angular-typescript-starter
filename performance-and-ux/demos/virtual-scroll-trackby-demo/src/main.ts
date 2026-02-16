const items = Array.from({ length: 5 }).map((_, i) => ({ id: `p${i}`, name: `Patient ${i}` }));

function render(list: typeof items) {
  console.log('render list ids:', list.map(i => i.id).join(','));
}

render(items);
// reorder but keep same ids
const reordered = [items[2], items[1], items[0], items[3], items[4]];
render(reordered);
console.log('trackBy id keeps DOM nodes stable');
