# 12) UI composition: theming + accessibility

লেম্যান-বাংলা: থিম টোকেন (রঙ/স্পেসিং) + কম্পোনেন্ট কম্পোজিশন + a11y গার্ড একসাথে রাখুন। নিচের CLI ডেমোতে contrast চেক ও accessible name বাধ্যতামূলক।

## Things to learn (beginner → intermediate → advanced)
- Beginner: design tokens (color, radius, spacing) আলাদা রাখা; কম্পোনেন্ট ফাংশন টোকেন খায়, CSS স্ট্রিং বানায়।
- Intermediate: WCAG contrast >= 4.5 যাচাই; aria-label/visible label বাধ্যতামূলক; light/dark theme swap।
- Advanced: semantic tokens (surface/primary/onPrimary), motion-reduce টোকেন, per-component slot override, theme-driven states (hover/disabled) ।

## Hands-on (commands + কী দেখবেন)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/ui-composition-theming-a11y-demo
   npm install
   npm run demo       # light + dark button render + contrast + a11y guard
   npm run typecheck  # টাইপ সেফটি
   ```
2) Expected আউটপুট: light/dark দুই থিমে button HTML ও contrast মেটা; শেষ লাইনে missing label এর জন্য error।
3) Break/fix: `tokens.ts` এ onPrimary হালকা করে contrast error ট্রিগার করুন; `composeButton` এ `secondary` variant যোগ করুন; `assertContrast` এর থ্রেশহোল্ড 7.0 করে কঠোর করুন।

## Demos (copy-paste)
`architecture-and-state/demos/ui-composition-theming-a11y-demo/src/` থেকে মূল ফাইল:
```ts
// tokens.ts
export type Theme = { name: 'light' | 'dark'; colors: { bg: string; surface: string; primary: string; onPrimary: string; text: string; muted: string }; radius: string; spacing: (step: 1|2|3|4) => string; };
export const lightTheme: Theme = { name: 'light', colors: { bg: '#f8fafc', surface: '#ffffff', primary: '#2563eb', onPrimary: '#ffffff', text: '#0f172a', muted: '#475569' }, radius: '8px', spacing: s => `${s*4}px` };
export const darkTheme: Theme = { name: 'dark', colors: { bg: '#0f172a', surface: '#111827', primary: '#60a5fa', onPrimary: '#0b1221', text: '#e2e8f0', muted: '#94a3b8' }, radius: '8px', spacing: s => `${s*4}px` };
```
```ts
// a11y.ts
export function contrastRatio(fgHex: string, bgHex: string): number { /* WCAG calc */ }
export function assertContrast(fgHex: string, bgHex: string, min = 4.5) { /* throws if low */ }
```
```ts
// components.ts
import { Theme } from './tokens';
import { assertContrast } from './a11y';
export function composeButton(theme: Theme, props: { label: string; ariaLabel?: string; variant?: 'primary'|'ghost'; disabled?: boolean }) {
  if (!props.label && !props.ariaLabel) throw new Error('Accessible name required');
  const variant = props.variant ?? 'primary';
  const styles = variant === 'primary'
    ? { background: theme.colors.primary, color: theme.colors.onPrimary, border: `1px solid ${theme.colors.primary}` }
    : { background: 'transparent', color: theme.colors.text, border: `1px solid ${theme.colors.muted}` };
  const ratio = assertContrast(styles.color, variant === 'primary' ? styles.background : theme.colors.surface, 4.5);
  return { role: 'button', ariaLabel: props.ariaLabel ?? props.label, styles: { ...styles, padding: `${theme.spacing(2)} ${theme.spacing(3)}`, borderRadius: theme.radius }, meta: { theme: theme.name, variant, contrast: ratio.toFixed(2) } };
}
export function renderButton(vnode: ReturnType<typeof composeButton>) { /* returns HTML string */ }
```
```ts
// main.ts
import { lightTheme, darkTheme } from './tokens';
import { composeButton, renderButton } from './components';
function demo(themeName: 'light' | 'dark') { const theme = themeName==='light'?lightTheme:darkTheme; const primary = composeButton(theme,{label:'Save'}); const ghost = composeButton(theme,{label:'Cancel',variant:'ghost'}); console.log(renderButton(primary), primary.meta); console.log(renderButton(ghost), ghost.meta); }
try { demo('light'); demo('dark'); composeButton(lightTheme,{label:''}); } catch(e){ console.error(e);} 
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/ui-composition-theming-a11y-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/ui-composition-theming-a11y-demo
  npm install
  npm run demo
  npm run typecheck
  ```
- Expected output: light/dark button HTML + contrast meta; missing label case throws a11y error.
- Test ideas: contrast fail করে দেখুন; নতুন variant যোগ করুন; assertContrast মিন 7.0 করুন।

## Common mistakes
- টোকেন hardcode করে কম্পোনেন্টে রাখা (re-use কঠিন)।
- contrast না মেপে primary/onPrimary বাছা।
- aria-label বা visible label বাদ দিয়ে বাটন রেন্ডার করা।

## Interview points
- Token → component → theme separation; WCAG 2.1 contrast 4.5/3.0 ন্যূনতম।
- Light/dark swapping without rewriting components (tokens only)।
- a11y guards build-time/lint/runtime কিভাবে বসাবেন।

## Quick practice
- `npm run demo` চালিয়ে meta contrast পড়ুন।
- নতুন semantic color (e.g., danger) যোগ করে contrast চেক করুন।
- টোকেন ফাইলে spacing বদলে padding কীভাবে বদলায় দেখুন।
