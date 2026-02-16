// Simple schema-driven form renderer
const schema = [
  { name: 'patient', label: 'Patient Name', type: 'text', required: true },
  { name: 'age', label: 'Age', type: 'number', min: 0 },
  { name: 'ward', label: 'Ward', type: 'select', options: ['A', 'B', 'ICU'] }
];

function renderField(field: any) {
  if (field.type === 'select') return `<label>${field.label}<select>${field.options.map((o: string)=>`<option>${o}</option>`).join('')}</select></label>`;
  return `<label>${field.label}<input type="${field.type}" ${field.required?'required':''} ${field.min!==undefined?`min=${field.min}`:''}></label>`;
}

console.log('Schema:', schema);
console.log('Rendered HTML:');
schema.forEach(f => console.log(renderField(f)));
