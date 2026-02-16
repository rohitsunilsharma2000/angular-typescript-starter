// Simulate server + client validation messages
const serverErrors = {
  fieldErrors: {
    email: 'Email already taken',
    ward: 'Ward closed'
  },
  message: 'Validation failed'
};

const clientErrors = {
  email: 'Invalid format',
  age: 'Must be >= 0'
};

function mergeErrors() {
  const merged: Record<string,string[]> = {};
  for (const [k,v] of Object.entries(clientErrors)) merged[k] = [v];
  for (const [k,v] of Object.entries(serverErrors.fieldErrors)) {
    merged[k] = merged[k] ? [...merged[k], v] : [v];
  }
  return merged;
}

console.log('Client errors:', clientErrors);
console.log('Server errors:', serverErrors.fieldErrors);
console.log('Merged:', mergeErrors());
