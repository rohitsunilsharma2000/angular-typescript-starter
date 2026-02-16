// Light/Dark theme switcher
const light = { name: 'light', bg: '#f8fafc', text: '#0f172a', primary: '#2563eb', onPrimary: '#ffffff' };
const dark  = { name: 'dark',  bg: '#0f172a', text: '#e2e8f0', primary: '#60a5fa', onPrimary: '#0b1221' };

function render(theme: typeof light) {
  const card = `background:${theme.bg};color:${theme.text};padding:12px;border-radius:8px`;
  const button = `background:${theme.primary};color:${theme.onPrimary};padding:8px 12px;border-radius:8px`;
  return { theme: theme.name, card, button };
}

console.log('Light:', render(light));
console.log('Dark :', render(dark));
