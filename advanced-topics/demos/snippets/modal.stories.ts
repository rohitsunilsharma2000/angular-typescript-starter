import type { Meta, StoryObj } from '@storybook/angular';
import { Component, Input } from '@angular/core';
import { userEvent, within } from '@storybook/testing-library';
import { expect } from '@storybook/jest';

@Component({
  standalone: true,
  selector: 'hms-modal',
  template: `
  <div *ngIf="open" class="modal">
    <header>
      <h3>{{ title }}</h3>
      <button (click)="close()">Close</button>
    </header>
    <ng-content></ng-content>
  </div>`,
  styles: [`.modal{padding:16px;border:1px solid #cbd5e1;border-radius:10px;}`]
})
class ModalComponent {
  @Input() open = true;
  @Input() title = 'Modal';
  close() { this.open = false; }
}

const meta: Meta<ModalComponent> = {
  title: 'HMS/Modal',
  component: ModalComponent,
  args: { open: true, title: 'Discharge confirmation' }
};
export default meta;
type Story = StoryObj<ModalComponent>;

export const Default: Story = {};
export const CloseInteraction: Story = {
  play: async ({ canvasElement }) => {
    const c = within(canvasElement);
    await userEvent.click(c.getByRole('button', { name: /close/i }));
    expect(c.queryByText(/discharge confirmation/i)).toBeNull();
  }
};
