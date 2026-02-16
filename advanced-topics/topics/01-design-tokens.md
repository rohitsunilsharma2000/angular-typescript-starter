# 01) Design tokens

রঙ/spacing/টাইপ/রেডিয়াস/মোশন ঠিকমতো না হলে HMS UI অমিল লাগে (patient card ও billing টেবিল অসামঞ্জস্য)। এই টপিকে CSS variables ভিত্তিক tokens।

## Why this matters (real-world)
- Multi-clinic ব্র্যান্ড বদলাতে কোড না ছুঁয়ে থিম পরিবর্তন সম্ভব।
- অডিটে hardcoded রঙ ধরা পড়ে না।

## Concepts
### Beginner
- Token categories: color/spacing/radius/typography/motion।
- CSS custom properties root এ সেট।
### Intermediate
- Alias বনাম semantic token (primary, surface, success, danger)।
- Dark mode toggle: prefers-color-scheme বা data-theme।
### Advanced
- Multi-brand mapping; design token JSON → CSS vars build; theming via runtime class।

## Copy-paste Example
```ts
// advanced-topics/demos/snippets/theme-tokens.ts
export const themeTokens = {
  color: {
    primary: '#2563eb',
    surface: '#ffffff',
    surfaceAlt: '#f8fafc',
    text: '#0f172a',
    success: '#16a34a',
    danger: '#dc2626'
  },
  spacing: { 1: '4px', 2: '8px', 3: '12px', 4: '16px' },
  radius: { sm: '6px', md: '10px' },
  motion: { fast: '120ms', base: '200ms' }
};
```
```css
/* styles.css */
:root {
  --color-primary: #2563eb;
  --color-surface: #ffffff;
  --color-text: #0f172a;
  --spacing-3: 12px;
  --radius-md: 10px;
  --motion-base: 200ms;
}
[data-theme='dark'] {
  --color-surface: #0b1220;
  --color-text: #e2e8f0;
}
```
```html
<!-- patient-card.component.html -->
<article class="card">
  <h3>{{ name }}</h3>
  <p>{{ ward }}</p>
</article>
```
```css
/* patient-card.component.css */
.card {
  background: var(--color-surface);
  color: var(--color-text);
  padding: var(--spacing-3);
  border-radius: var(--radius-md);
  transition: background var(--motion-base);
}
```

## Try it
- Beginner: data-theme="dark" যোগ করে surface/text পরিবর্তন দেখুন।
- Advanced: tokens JSON থেকে CSS vars জেনারেট স্ক্রিপ্ট লিখুন।

## Common mistakes
- Semantic token ছাড়া raw hex everywhere।
- Dark theme শুধু body bg বদলে টেক্সট contrast ভুলে যাওয়া।

## Interview points
- Design token বনাম component styles; CSS vars; semantic mapping; dark mode toggle।

## Done when…
- Tokens ফাইল ও CSS vars সেট।
- Component এ hardcoded color/spacing নেই।
