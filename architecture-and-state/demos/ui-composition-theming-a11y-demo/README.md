# UI composition: theming + accessibility demo

Console demo that composes a button with theme tokens, checks WCAG contrast, and enforces accessible names.

## Commands
```bash
npm install
npm run demo       # ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (trimmed)
```
=== light primary ===
<button ...>Save</button>
meta: { theme: 'light', variant: 'primary', contrast: '5.27' }

=== dark ghost ===
<button ...>Cancel</button>
meta: { theme: 'dark', variant: 'ghost', contrast: '12.07' }

=== a11y guard (missing label) ===
Expected error: Accessible name required: provide label or ariaLabel
```

## Break/fix ideas
- Lower the primary contrast (e.g., set onPrimary to '#999') and rerun to see contrast error.
- Add a new token (secondary) and extend `composeButton` to handle it.
- Swap to dark theme in code and confirm contrast still >= 4.5.
