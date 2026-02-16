// Mock of Storybook interaction test
function renderButton(label: string, disabled = false) {
  return `<button ${disabled ? 'disabled' : ''}>${label}</button>`;
}

function play() {
  const html = renderButton('Save');
  if (!html.includes('Save')) throw new Error('render failed: label missing');
  console.log('play: click simulated, assert label ok');
}

console.log('render:', renderButton('Save'));
play();
