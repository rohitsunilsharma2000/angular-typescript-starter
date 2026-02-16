import type { Meta, StoryObj } from '@storybook/angular';
import { Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'hms-input',
  template: `<label>{{ label }}<input [placeholder]="placeholder" /></label>`,
  styles: [`label{display:flex;flex-direction:column;gap:4px;} input{padding:8px;border:1px solid #cbd5e1;border-radius:8px;}`]
})
class InputComponent {
  @Input() label = 'Patient Name';
  @Input() placeholder = 'Rima';
}

const meta: Meta<InputComponent> = {
  title: 'HMS/Input',
  component: InputComponent,
  args: { label: 'Patient Name', placeholder: 'Rima' },
};
export default meta;
type Story = StoryObj<InputComponent>;
export const Default: Story = {};
