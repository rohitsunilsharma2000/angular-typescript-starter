import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'hms-skeleton-loader',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<div class="skeleton" [style.height.px]="height" [style.width.%]="width"></div>`,
  styles: [`
    .skeleton { background: linear-gradient(90deg, #e2e8f0 25%, #f8fafc 37%, #e2e8f0 63%); background-size: 400% 100%; animation: shimmer 1.4s ease infinite; border-radius: 8px; }
    @keyframes shimmer { 0% { background-position: 100% 0; } 100% { background-position: -100% 0; } }
  `]
})
export class SkeletonLoaderComponent {
  @Input() height = 56;
  @Input() width = 100;
}
