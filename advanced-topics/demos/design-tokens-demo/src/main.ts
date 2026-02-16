// Minimal design tokens and consumption
const tokens = {
  colors: {
    primary: '#2563eb',
    onPrimary: '#ffffff',
    surface: '#f8fafc',
    text: '#0f172a'
  },
  radius: '8px',
  spacing: (n: number) => `${n * 4}px`
};

const button = {
  label: 'Save',
  style: `background:${tokens.colors.primary};color:${tokens.colors.onPrimary};padding:${tokens.spacing(2)} ${tokens.spacing(3)};border-radius:${tokens.radius};border:1px solid ${tokens.colors.primary};`
};

const card = {
  title: 'Patient',
  style: `background:${tokens.colors.surface};color:${tokens.colors.text};padding:${tokens.spacing(3)};border-radius:${tokens.radius};`
};

console.log('Tokens:', tokens);
console.log('Button style:', button.style);
console.log('Card style:', card.style);
