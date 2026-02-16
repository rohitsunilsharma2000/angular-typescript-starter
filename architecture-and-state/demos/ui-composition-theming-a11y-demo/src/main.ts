import { lightTheme, darkTheme } from './tokens';
import { composeButton, renderButton } from './components';

function log(title: string, html: string, meta: any) {
  console.log(`\n=== ${title} ===`);
  console.log(html);
  console.log('meta:', meta);
}

function demo(themeName: 'light' | 'dark') {
  const theme = themeName === 'light' ? lightTheme : darkTheme;
  const primary = composeButton(theme, { label: 'Save', variant: 'primary' });
  const ghost = composeButton(theme, { label: 'Cancel', variant: 'ghost' });
  log(`${theme.name} primary`, renderButton(primary), primary.meta);
  log(`${theme.name} ghost`, renderButton(ghost), ghost.meta);
}

try {
  demo('light');
  demo('dark');

  console.log('\n=== a11y guard (missing label) ===');
  try {
    // should throw
    // @ts-expect-error demo invalid input
    composeButton(lightTheme, { label: '' });
  } catch (e: any) {
    console.error('Expected error:', e.message);
  }
} catch (err) {
  console.error('Unexpected error', err);
}
