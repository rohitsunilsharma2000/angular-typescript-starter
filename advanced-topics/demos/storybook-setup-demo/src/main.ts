// Mock Storybook metadata + play function (console only)
const meta = {
  title: 'Buttons/Primary',
  component: 'ButtonComponent',
  args: { label: 'Save', disabled: false }
};

function render(args: typeof meta.args) {
  return `<button ${args.disabled ? 'disabled' : ''}>${args.label}</button>`;
}

async function play() {
  const html = render(meta.args);
  console.log('rendered:', html);
  console.log('play: click simulated');
}

console.log('Story meta:', meta);
play();
