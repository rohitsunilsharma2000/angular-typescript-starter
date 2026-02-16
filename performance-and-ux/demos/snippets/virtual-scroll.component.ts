import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CdkVirtualScrollViewport, ScrollingModule } from '@angular/cdk/scrolling';

type Bed = { id: string; ward: string; occupied: boolean };

@Component({
  standalone: true,
  selector: 'hms-virtual-scroll-demo',
  imports: [CommonModule, ScrollingModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <cdk-virtual-scroll-viewport itemSize="56" class="h-96 w-full border rounded">
      <div *cdkVirtualFor="let bed of beds; trackBy: trackById" class="flex justify-between px-3 py-2 border-b">
        <span>{{ bed.id }} ({{ bed.ward }})</span>
        <span [class.text-green-600]="!bed.occupied" [class.text-red-600]="bed.occupied">
          {{ bed.occupied ? 'Occupied' : 'Free' }}
        </span>
      </div>
    </cdk-virtual-scroll-viewport>
  `
})
export class VirtualScrollComponent {
  beds: Bed[] = Array.from({ length: 3000 }).map((_, i) => ({
    id: 'B' + i,
    ward: i % 2 === 0 ? 'ICU' : 'GEN',
    occupied: i % 3 === 0,
  }));
  trackById = (_: number, bed: Bed) => bed.id;
}
