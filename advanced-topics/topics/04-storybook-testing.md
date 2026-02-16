# 04) Storybook testing (snapshot + interaction)

Appointment row, modal, toast স্টোরি থেকে regression ও interaction টেস্ট চালিয়ে UI স্থিতিশীল রাখুন।

## Why this matters (real-world)
- PR এ UI পরিবর্তন দুর্ঘটনাবশত ধরা যায়।
- Behavior (click, keyboard) স্টোরি থেকেই টেস্ট করা যায়।

## Concepts
### Beginner
- Storybook test runner (playwright under the hood) ও interaction test (`play`).
- Limited snapshot where layout critical।
### Intermediate
- `play` ফাংশনে userEvent; expect assertions।
- Visual/interaction split; controls-driven stories।
### Advanced
- CI integration; per-PR Storybook tests; Chromatic/visual optional।

## Copy-paste Example
```ts
// advanced-topics/demos/snippets/modal.stories.ts
import type { Meta, StoryObj } from '@storybook/angular';
import { ModalComponent } from './modal.component';
import { userEvent, within } from '@storybook/testing-library';
import { expect } from '@storybook/jest';

const meta: Meta<ModalComponent> = {
  title: 'HMS/Modal',
  component: ModalComponent,
  args: { open: true, title: 'Discharge confirmation' },
};
export default meta;
type Story = StoryObj<ModalComponent>;

export const Default: Story = {};

export const CloseInteraction: Story = {
  play: async ({ canvasElement }) => {
    const c = within(canvasElement);
    await userEvent.click(c.getByRole('button', { name: /close/i }));
    expect(c.queryByText(/discharge confirmation/i)).toBeNull();
  },
};
```
```bash
# Install test runner
npx storybook@latest test init
npm run test-storybook
```

## Try it
- Beginner: play function এ বোতাম ক্লিক assertion যোগ করুন।
- Advanced: CI এ `npm run build-storybook && npm run test-storybook` যোগ করুন।

## Common mistakes
- Snapshot overuse (fragile). Interaction better।
- Async waits না দিয়ে flake।

## Interview points
- Storybook play interaction; when to snapshot; CI integration।

## Done when…
- অন্তত এক interaction test আছে।
- CI তে build-storybook + test-storybook চলে।
