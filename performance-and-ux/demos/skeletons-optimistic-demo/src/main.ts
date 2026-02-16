const skeleton = '[████     ] Loading patient list...';
const data = ['Patient 1', 'Patient 2'];
const optimistic = [...data, 'New patient (optimistic)'];

console.log('Skeleton:', skeleton);
console.log('Real data:', data);
console.log('Optimistic state:', optimistic);
