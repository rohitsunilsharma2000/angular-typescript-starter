import type { Meta, StoryObj } from '@storybook/angular';
import { Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'hms-button',
  template: `<button [ngClass]="kind">{{ label }}</button>`,
  styles: [
    `.primary { background:#2563eb; color:white; padding:8px 12px; border-radius:8px; }`,
    `.ghost { background:transparent; color:#2563eb; padding:8px 12px; border:1px solid #2563eb; border-radius:8px; }`
  ]
})
class ButtonComponent {
  @Input() label = 'Button';
  @Input() kind: 'primary' | 'ghost' = 'primary';
}

const meta: Meta<ButtonComponent> = {
  title: 'HMS/Button',
  component: ButtonComponent,
  args: { label: 'Admit patient', kind: 'primary' },
};
export default meta;
type Story = StoryObj<ButtonComponent>;
export const Primary: Story = {};
export const Ghost: Story = { args: { kind: 'ghost', label: 'View invoices' } };
