# 03) Storybook setup

Patient card, appointment row, billing badge—Storybook এ রেফারেন্স রাখুন যাতে UI/UX ও dev টিম দ্রুত মান ঠিক করতে পারে।

## Why this matters (real-world)
- UI drift কমে; QA/PM ডেমো সহজ।
- Stories = living spec; regression ধরা যায়।

## Concepts
### Beginner
- Storybook install, `npm run storybook`; stories ফাইল naming।
- CSF with args/controls।
### Intermediate
- Docs page, global decorators (theme/i18n), assets handling।
- Chromatic/CI build basics।
### Advanced
- Performance of Storybook build; static export; per-PR deploy; addon-essentials tuning।

## Copy-paste Example
```bash
# Install
npx storybook@latest init
```
```ts
// advanced-topics/demos/snippets/button.stories.ts
import type { Meta, StoryObj } from '@storybook/angular';
import { ButtonComponent } from './button.component';
const meta: Meta<ButtonComponent> = {
  title: 'HMS/Button',
  component: ButtonComponent,
  args: { label: 'Admit patient', kind: 'primary' },
  parameters: { controls: { expanded: true } },
};
export default meta;
type Story = StoryObj<ButtonComponent>;
export const Primary: Story = {};
export const Ghost: Story = { args: { kind: 'ghost', label: 'View invoices' } };
```
```json
// package.json scripts (excerpt)
{
  "scripts": {
    "storybook": "storybook dev -p 6006",
    "build-storybook": "storybook build"
  }
}
```

## Try it
- Beginner: button story চালান; controls দিয়ে props বদলান।
- Advanced: docs tabে MDX নোট যোগ করুন ও GitHub Actions এ `build-storybook` চালান।

## Common mistakes
- stories এ random data → flaky screenshots।
- Controls না দিয়ে props manual = UX কম।

## Interview points
- Storybook = spec; args/controls; CI build।

## Done when…
- Storybook চলে; অন্তত 1 story আছে।
- `build-storybook` স্ক্রিপ্ট যোগ।
