import type { Meta, StoryObj } from '@storybook/angular';
import { Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'hms-toast',
  template: `<div class="toast" [class.error]="type==='error'">{{ message }}</div>`,
  styles: [`.toast{padding:10px;border-radius:8px;background:#e0f2fe;color:#0f172a;} .toast.error{background:#fee2e2;color:#991b1b;}`]
})
class ToastComponent {
  @Input() message = 'Saved';
  @Input() type: 'info' | 'error' = 'info';
}

const meta: Meta<ToastComponent> = {
  title: 'HMS/Toast',
  component: ToastComponent,
  args: { message: 'Patient saved', type: 'info' }
};
export default meta;
type Story = StoryObj<ToastComponent>;
export const Info: Story = {};
export const Error: Story = { args: { message: 'Failed to save', type: 'error' } };
