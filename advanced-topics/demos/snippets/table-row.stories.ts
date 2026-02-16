import type { Meta, StoryObj } from '@storybook/angular';
import { Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'hms-table-row',
  template: `<div class="row"><span>{{ patient }}</span><span>{{ ward }}</span><span>{{ status }}</span></div>`,
  styles: [`.row{display:grid;grid-template-columns:1fr 1fr 1fr;gap:8px;padding:8px;border-bottom:1px solid #e2e8f0;}`]
})
class TableRowComponent {
  @Input() patient = 'Rima';
  @Input() ward = 'ICU-1';
  @Input() status = 'Waiting';
}

const meta: Meta<TableRowComponent> = {
  title: 'HMS/TableRow',
  component: TableRowComponent,
  args: { patient: 'Rima', ward: 'ICU-1', status: 'Waiting' }
};
export default meta;
type Story = StoryObj<TableRowComponent>;
export const Default: Story = {};
